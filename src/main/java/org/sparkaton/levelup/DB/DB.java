package org.sparkaton.levelup.DB;

import org.sparkaton.levelup.dto.Quiz;
import org.sparkaton.levelup.dto.UserQuiz;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DB {


    private static AtomicInteger QUIZ_ID=new AtomicInteger(1);
    private static Map<Integer, Quiz> QUIZ_MAP=new HashMap<>();

    public static int getQuizID() {
        return QUIZ_ID.getAndAdd(1);
    }
    public static void addQuiz(int quizId,Quiz quiz) {
        QUIZ_MAP.put(quizId,quiz);
    }
    public static Quiz getQuiz(int quizId) {
        return QUIZ_MAP.get(quizId);
    }
    public static Quiz getLastQuiz() {
        return QUIZ_MAP.get(QUIZ_ID.get()-1);
    }




    private static Map<Integer, Map<Integer, UserQuiz>> USER_QUIZ_MAP=new HashMap<>();
    public static void addUserQuiz(int userId, int quizId, UserQuiz userQuiz) {
        if (!USER_QUIZ_MAP.keySet().contains(userId)) {
            Map<Integer, UserQuiz> newUserQuiz =new HashMap<>();
            USER_QUIZ_MAP.put(userId, newUserQuiz);
        }
        USER_QUIZ_MAP.get(userId).put(quizId,userQuiz);
    }
}
