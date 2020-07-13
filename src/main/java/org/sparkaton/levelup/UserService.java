package org.sparkaton.levelup;

import org.sparkaton.levelup.DB.DB;
import org.sparkaton.levelup.dto.UserAnswer;
import org.sparkaton.levelup.dto.UserQuiz;

public class UserService {
    public UserService(){

        setDefaultUserquizes();
    }

    private void setDefaultUserquizes() {
//        UserQuiz userQuiz1=new UserQuiz();
    }

    public void addUserQuiz(int userId, UserQuiz userQuiz) {

        DB.addUserQuiz(userId,userQuiz.getQuizId(),userQuiz);

    }
}
