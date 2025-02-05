package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class bookrecommend extends AbstractFormPlugin {
    // 用户-图书借阅历史
    private static final Map<String, String[]> USER_BOOK_HISTORY = new HashMap<>();
    private static final Map<String, Integer> BOOK_BORROW_COUNT = new HashMap<>();
    private static final Map<String, String> BOOK_CATEGORIES = new HashMap<>();
    private void getUserBookHistory(){
        DynamicObject[] person=BusinessDataServiceHelper.load("kxr1_perinform","id,name,number",null);
        for(DynamicObject p:person){
            QFilter qFilter=new QFilter("creator.number", QCP.equals,p.get("number"));
            DynamicObject[] bookhistory =
                    BusinessDataServiceHelper.load("kxr1_bookshelf","id,kxr1_basedatafield.name,kxr1_basedatafield.kxr1_textfield,kxr1_basedatafield.kxr1_textfield1,kxr1_basedatafield.group.name,creator.number",new QFilter[]{qFilter});
            String[] bookn=new String[bookhistory.length];
            for(int i=0;i<bookhistory.length;i++){
                String bookname=bookhistory[i].get("kxr1_basedatafield.name").toString();
                bookn[i]=bookname;
                //int bookrecords=Integer.parseInt(book.get("kxr1_basedatafield.kxr1_integerfield1").toString());

            }
            USER_BOOK_HISTORY.put(p.get("name").toString(), bookn);
        }

    }
    private void getBookBorrowCount(){
        DynamicObject[] bookrecords=BusinessDataServiceHelper.load("kxr1_book","name,kxr1_integerfield1",null);
        for(DynamicObject b:bookrecords){
            int records=Integer.parseInt(b.get("kxr1_integerfield1").toString());
            String name=b.get("name").toString();
            BOOK_BORROW_COUNT.put(name,records);
        }
    }
    private void getBookCategories(){
        DynamicObject[] bookrecords=BusinessDataServiceHelper.load("kxr1_book","name,group.name",null);
        for(DynamicObject b:bookrecords){
            String name=b.get("name").toString();
            String group=b.get("group.name").toString();
            BOOK_CATEGORIES.put(name,group);
        }

    }


    public static double calculateUserSimilarity(String user1, String user2) {

        String[] user1History = USER_BOOK_HISTORY.get(user1);
        String[] user2History = USER_BOOK_HISTORY.get(user2);
        if(user1History==null||user2History==null){
            return 0;
        }

        // 将用户历史转换为向量
        int[] user1Vector = new int[getUniqueBookCount(user1History, user2History)];
        int[] user2Vector = new int[getUniqueBookCount(user1History, user2History)];

        for (int i = 0; i < getUniqueBookCount(user1History, user2History); i++) {
            user1Vector[i] = countBookInArray(user1History, getUniqueBooks(user1History, user2History)[i]);
            user2Vector[i] = countBookInArray(user2History, getUniqueBooks(user1History, user2History)[i]);
        }

        // 计算用户之间的余弦相似度
        return 1 - cosineSimilarity(user1Vector, user2Vector);
    }

    public static double calculateBookScore(String user, String book, String[] userHistory, Map<String, Integer> bookCount, Map<String, String> bookCategories) {
        // 基于用户历史的得分
        double userHistoryScore;
        if(userHistory==null){
            // 基于用户历史的得分
             userHistoryScore = 0;
        }else{
            // 基于用户历史的得分
            userHistoryScore = containsBook(userHistory, book) ? 1 : 0;
        }

        // 基于图书流行度的得分
        double bookPopularityScore = Math.log(bookCount.get(book));

        // 基于图书类别的得分
        double bookCategoryScore = 0;
        String[] categories = bookCategories.get(book).split(",");
        for (String category : categories) {
            bookCategoryScore += 1 / (1 + Math.exp(-categories.length));
        }

        return userHistoryScore + bookPopularityScore + bookCategoryScore;
    }


    public static List<Recommendation> recommendBooks(String user, int topN) {
        // 计算所有用户与当前用户的相似度
        Map<String, Double> userSimilarities = new HashMap<>();
        for (String otherUser : USER_BOOK_HISTORY.keySet()) {
            if (!otherUser.equals(user)) {
                userSimilarities.put(otherUser, calculateUserSimilarity(user, otherUser));
            }
        }

        // 根据用户相似度加权计算每本图书的得分
        Map<String, Double> bookScores = new HashMap<>();
        for (Map.Entry<String, Double> entry : userSimilarities.entrySet()) {
            String otherUser = entry.getKey();
            double similarity = entry.getValue();
            for (String book : USER_BOOK_HISTORY.get(otherUser)) {
                bookScores.merge(book, similarity * calculateBookScore(user, book, USER_BOOK_HISTORY.get(user), BOOK_BORROW_COUNT, BOOK_CATEGORIES), Double::sum);
            }
        }

        // 按得分从高到低排序并返回前 topN 个图书
        List<Recommendation> recommendations = bookScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((a, b) -> Double.compare(b, a)))
                .map(e -> new Recommendation(e.getKey(), e.getValue()))
                .limit(topN)
                .collect(Collectors.toList());

        return recommendations;
    }

    public static class Recommendation {
        private final String book;
        private final double score;

        public Recommendation(String book, double score) {
            this.book = book;
            this.score = score;
        }

        public String getBook() {
            return book;
        }

        public double getScore() {
            return score;
        }
    }
    private static int getUniqueBookCount(String[] user1History, String[] user2History) {
        return (int) getUniqueBooks(user1History, user2History).length;
    }

    private static String[] getUniqueBooks(String[] user1History, String[] user2History) {
        return Stream.concat(Arrays.stream(user1History), Arrays.stream(user2History))
                .distinct()
                .toArray(String[]::new);
    }

    private static int countBookInArray(String[] array, String book) {
        int count = 0;

        for (String b : array) {
            if (b.equals(book)) {
                count++;
            }
        }
        return count;
    }

    private static boolean containsBook(String[] array, String book) {

        for (String b : array) {
            if (b.equals(book)) {
                return true;
            }
        }
        return false;
    }

    private static double cosineSimilarity(int[] vector1, int[] vector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            magnitude1 += vector1[i] * vector1[i];
            magnitude2 += vector2[i] * vector2[i];
        }
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        return dotProduct / (magnitude1 * magnitude2);
    }

    @Override
    public void afterCreateNewData(EventObject e) {
        super.afterCreateNewData(e);
        getBookBorrowCount();
        getBookCategories();
        getUserBookHistory();
        Long userId= RequestContext.get().getCurrUserId();
        String name= UserServiceHelper.getUserInfoByID(userId).get("name").toString();
        List<Recommendation> recommendations= recommendBooks(name,10);
        int index=0;
        do {
            String bookname = recommendations.get(index).getBook();
            QFilter nameFilter = new QFilter("name", QCP.equals, bookname);
            DynamicObject book = BusinessDataServiceHelper.loadSingle("kxr1_book", "id,number,name,kxr1_textfield,kxr1_textfield1,kxr1_integerfield", new QFilter[]{nameFilter});

            this.getModel().setValue("kxr1_basedatafield", book, index);
            index++; // 更新计数器
            if(index<recommendations.size()){
                this.getModel().createNewEntryRow("kxr1_entryentity");
            }


        } while (index <recommendations.size()); // 循环条件
    }
}



