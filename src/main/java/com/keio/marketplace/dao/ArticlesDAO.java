package com.keio.marketplace.dao;

import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.utils.CreateIDUtil;
import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.AbortException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ArticlesDAO {
    private final DistributedTransactionManager manager;

    private static final String ARTICLES_NAMESPACE = "marketplace";
    private static final String ARTICLES_TABLE = "articles";

    public ArticlesDAO() throws IOException {
        Properties loadProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("META-INF/articles.properties")) {
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

    public void addArticle(
            long id,
            String name,
            int price,
            int stock,
            String image
    ) throws Exception {

        Articles article = getArticle(name);
        DistributedTransaction transaction = null;

        try {
            transaction = manager.start();
            if (article == null) {
                transaction.put(
                        Put.newBuilder()
                                .namespace(ARTICLES_NAMESPACE)
                                .table(ARTICLES_TABLE)
                                .partitionKey(Key.ofBigInt("id", id))
                                .textValue("name", name)
                                .intValue("price", price)
                                .intValue("stock", stock)
                                .textValue("image", image)
                                .build());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    public void addArticle(
            String name,
            int price,
            int stock,
            String image
    ) throws Exception {
        long id = CreateIDUtil.createID();
        addArticle(id, name, price, stock, image);
    }

    public void updateArticle(Articles articles) throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Optional<Result> existingArticle = transaction.get(
                    Get.newBuilder()
                            .namespace(ARTICLES_NAMESPACE)
                            .table(ARTICLES_TABLE)
                            .partitionKey(Key.ofBigInt("id", articles.getId()))
                            .build()
            );
            if (existingArticle.isPresent()) {
                transaction.put(
                        Put.newBuilder()
                                .namespace(ARTICLES_NAMESPACE)
                                .table(ARTICLES_TABLE)
                                .partitionKey(Key.ofBigInt("id", articles.getId()))
                                .textValue("name", articles.getName())
                                .intValue("price", articles.getPrice())
                                .intValue("stock", articles.getStock())
                                .textValue("image", articles.getImage())
                                .build()
                );
                transaction.commit();
            } else {
                throw new Exception("Article does not exist and cannot be updated.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            e.printStackTrace();
        }
    }

//    public void updateArticle(Articles articles) throws AbortException {
//        DistributedTransaction transaction = null;
//        try {
//            transaction = manager.start();
//            transaction.put(
//                    Put.newBuilder()
//                            .namespace(ARTICLES_NAMESPACE)
//                            .table(ARTICLES_TABLE)
//                            .partitionKey(Key.ofBigInt("id", articles.getId()))
//                            .textValue("name", articles.getName())
//                            .intValue("price", articles.getPrice())
//                            .intValue("stock", articles.getStock())
//                            .textValue("image", articles.getImage())
//                            .build()
//            );
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.abort();
//            }
//            e.printStackTrace();
//        }
//    }

    public void deleteArticle(long id) throws AbortException {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            transaction.delete(
                    Delete.newBuilder()
                            .namespace(ARTICLES_NAMESPACE)
                            .table(ARTICLES_TABLE)
                            .partitionKey(Key.ofBigInt("id", id))
                            .build());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            e.printStackTrace();
        }
    }

    public void deleteArticle(Articles articles) throws AbortException {
        deleteArticle(articles.getId());
    }

    public void deleteArticle(String name) throws Exception {
        deleteArticle(getArticle(name).getId());
    }

    public Articles getArticle(long id) throws Exception {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Optional<Result> result =
                    transaction.get(
                            Get.newBuilder()
                                    .namespace(ARTICLES_NAMESPACE)
                                    .table(ARTICLES_TABLE)
                                    .partitionKey(Key.ofBigInt("id", id))
                                    .build());
            if (!result.isPresent()) {
                throw new Exception("Article not found");
            }

            transaction.commit();

            return new Articles(
                    result.get().getText("name"),
                    result.get().getInt("price"),
                    result.get().getInt("stock"),
                    result.get().getText("image"),
                    result.get().getBigInt("id")
            );
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    public Articles getArticle(String name) throws Exception {
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Scan scan = Scan.newBuilder()
                    .namespace(ARTICLES_NAMESPACE)
                    .table(ARTICLES_TABLE)
                    .all().build();

            List<Result> resultList = transaction.scan(scan);
            for (Result result : resultList) {
                if (name.equals(result.getText("name"))) {
                    transaction.commit();
                    return new Articles(
                            result.getText("name"),
                            result.getInt("price"),
                            result.getInt("stock"),
                            result.getText("image"),
                            result.getBigInt("id")
                    );
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
        return null;
    }

    public List<Articles> getAllArticles() throws Exception {
        List<Articles> articlesList = new ArrayList<>();
        DistributedTransaction transaction = null;
        try {
            transaction = manager.start();
            Scan scan = Scan.newBuilder()
                    .namespace(ARTICLES_NAMESPACE)
                    .table(ARTICLES_TABLE)
                    .all().build();

            List<Result> results = transaction.scan(scan);
            for (Result result : results) {
                articlesList.add(new Articles(
                        result.getText("name"),
                        result.getInt("price"),
                        result.getInt("stock"),
                        result.getText("image"),
                        result.getBigInt("id")));
            }

            transaction.commit();
            return articlesList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.abort();
            }
            throw e;
        }
    }

    public boolean checkStock(Articles articles, int quantity) throws Exception {
        Articles updatedArticles = getArticle(articles.getId());
        return updatedArticles.getStock() >= quantity;
    }

    public boolean checkStock(String id, int quantity) throws Exception {
        Articles updatedArticles = getArticle(Long.parseLong(id));
        return updatedArticles.getStock() >= quantity;
    }
}