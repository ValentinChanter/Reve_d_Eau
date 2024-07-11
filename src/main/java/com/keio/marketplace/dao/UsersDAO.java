package com.keio.marketplace.dao;

import com.keio.marketplace.entity.Users;
import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.AbortException;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class UsersDAO {
    private final DistributedTransactionManager manager;

    private static final String USERS_NAMESPACE = "postgres";
    private static final String USERS_TABLE = "users";

    public UsersDAO() throws IOException {
        Properties loadProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("META-INF/users.properties")) {
            loadProperties.load(input);
        } catch (IOException e) {
            throw e;
        }

        TransactionFactory transactionFactory = TransactionFactory.create(loadProperties);
        manager = transactionFactory.getTransactionManager();
    }

    public void close() {
        manager.close();
    }

    public void addUser(Users user) throws Exception {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Optional<Result> article = transaction.get(
                Get.newBuilder()
                    .namespace(USERS_NAMESPACE)
                    .table(USERS_TABLE)
                    .partitionKey(Key.ofBigInt("id", user.getId()))
                    .build()
            );
            if (article.isEmpty()) {
                transaction.put(
                    Put.newBuilder()
                        .namespace(USERS_NAMESPACE)
                        .table(USERS_TABLE)
                        .partitionKey(Key.ofBigInt("id", user.getId()))
                        .textValue("email", user.getEmail())
                        .textValue("name", user.getName())
                        .textValue("password", user.getPassword())
                        .booleanValue("is_admin", user.getAdmin())
                        .intValue("loyalty_points", user.getLoyaltyPoints())
                        .textValue("cart", user.getCart())
                        .build()
                );
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    public void updateUser(Users user) throws TransactionException {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Optional<Result> existingUser = transaction.get(
                    Get.newBuilder()
                            .namespace(USERS_NAMESPACE)
                            .table(USERS_TABLE)
                            .partitionKey(Key.ofBigInt("id", user.getId()))
                            .build()
            );
            if (existingUser.isPresent()) {
                transaction.put(
                        Put.newBuilder()
                                .namespace(USERS_NAMESPACE)
                                .table(USERS_TABLE)
                                .partitionKey(Key.ofBigInt("id", user.getId()))
                                .textValue("email", user.getEmail())
                                .textValue("name", user.getName())
                                .textValue("password", user.getPassword())
                                .booleanValue("is_admin", user.getAdmin())
                                .intValue("loyalty_points", user.getLoyaltyPoints())
                                .textValue("cart", user.getCart())
                                .build()
                );
                transaction.commit();
            } else {
                throw new Exception("User does not exist and cannot be updated.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            e.printStackTrace();
        }
    }

    public void deleteUser(Users user) throws TransactionException {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            transaction.delete(
                Delete.newBuilder()
                    .namespace(USERS_NAMESPACE)
                    .table(USERS_TABLE)
                    .partitionKey(Key.ofBigInt("id", user.getId()))
                    .build()
            );
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    public void deleteUser(String email) throws TransactionException {
        deleteUser(getUser(email));
    }

    public void deleteUser(long id) throws TransactionException {
        deleteUser(getUser(id));
    }

    public Users getUser(long id) {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Optional<Result> user = transaction.get(
                Get.newBuilder()
                    .namespace(USERS_NAMESPACE)
                    .table(USERS_TABLE)
                    .partitionKey(Key.ofBigInt("id", id))
                    .build()
            );
            if (user.isEmpty()) {
                throw new Exception("User not found");
            }

            transaction.commit();

            return new Users(
                user.get().getText("email"),
                user.get().getText("name"),
                user.get().getText("password"),
                user.get().getBoolean("is_admin"),
                user.get().getInt("loyalty_points"),
                user.get().getText("cart"),
                user.get().getBigInt("id")
            );
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.abort();
                } catch (AbortException ae) {
                    ae.printStackTrace();
                }
            }
            return null;
        }
    }

    public Users getUser(String email) {
        DistributedTransaction transaction = null;
        try {
            List<Users> users = getAllUsers();

            return users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Users> getAllUsers() {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Scan scan = Scan.newBuilder()
                .namespace(USERS_NAMESPACE)
                .table(USERS_TABLE)
                .all()
                .build();

            List<Users> users = transaction.scan(scan).stream().map(result -> {
                return new Users(
                    result.getText("email"),
                    result.getText("name"),
                    result.getText("password"),
                    result.getBoolean("is_admin"),
                    result.getInt("loyalty_points"),
                    result.getText("cart"),
                    result.getBigInt("id")
                );
            }).toList();

            transaction.commit();
            return users;
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.abort();
                } catch (AbortException ae) {
                    ae.printStackTrace();
                }
            }
            return null;
        }
    }

    public List<Users> getUsersByName(String name) {
        try {
            List<Users> users = getAllUsers();

            return users.stream().filter(u -> u.getName().equals(name)).toList();
        } catch (Exception e) {
            return null;
        }
    }

    public Users getFirstUserByName(String name) {
        List<Users> users = getUsersByName(name);
        return users.isEmpty() ? null : users.get(0);
    }

    public boolean login(String email, String password) {
        Users user = getUser(email);
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(password, user.getPassword());
    }
}
