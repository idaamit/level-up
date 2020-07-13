package org.sparkaton.levelup;

import org.sparkaton.levelup.DB.DB;
import org.sparkaton.levelup.dto.UserAnswer;
import org.sparkaton.levelup.dto.UserQuiz;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    public UserService(){
        setDefaultUserQuizes();
    }



    public void addUserQuiz(int userId, UserQuiz userQuiz) {
        DB.addUserQuiz(userId,userQuiz.getQuizId(),userQuiz);
    }

    private void setDefaultUserQuizes() {
        List<UserAnswer> userAnswers1=new ArrayList<>();
        userAnswers1.add(new UserAnswer(2001,3,true));
        userAnswers1.add(new UserAnswer(2002,5,false));
        userAnswers1.add(new UserAnswer(2003,7,true));
        userAnswers1.add(new UserAnswer(2004,2,true));
        userAnswers1.add(new UserAnswer(2005,4,false));
        UserQuiz userQuiz1=new UserQuiz(20,userAnswers1);
        DB.addUserQuiz(1,20,userQuiz1);

        List<UserAnswer> userAnswers2=new ArrayList<>();
        userAnswers2.add(new UserAnswer(2001,7,false));
        userAnswers2.add(new UserAnswer(2002,8,false));
        userAnswers2.add(new UserAnswer(2003,7,false));
        userAnswers2.add(new UserAnswer(2004,5,true));
        userAnswers2.add(new UserAnswer(2005,9,false));
        UserQuiz userQuiz2=new UserQuiz(20,userAnswers2);
        DB.addUserQuiz(2,20,userQuiz2);


        List<UserAnswer> userAnswers3=new ArrayList<>();
        userAnswers3.add(new UserAnswer(2101,12,true));
        userAnswers3.add(new UserAnswer(2102,13,true));
        userAnswers3.add(new UserAnswer(2103,13,true));
        userAnswers3.add(new UserAnswer(2104,11,true));
        userAnswers3.add(new UserAnswer(2105,17,true));
        UserQuiz userQuiz3=new UserQuiz(21,userAnswers3);
        DB.addUserQuiz(1,21,userQuiz3);

        List<UserAnswer> userAnswers4=new ArrayList<>();
        userAnswers4.add(new UserAnswer(2101,17,false));
        userAnswers4.add(new UserAnswer(2102,18,false));
        userAnswers4.add(new UserAnswer(2103,17,false));
        userAnswers4.add(new UserAnswer(2104,16,false));
        userAnswers4.add(new UserAnswer(2105,19,false));
        UserQuiz userQuiz4=new UserQuiz(21,userAnswers4);
        DB.addUserQuiz(2,21,userQuiz4);


        List<UserAnswer> userAnswers5=new ArrayList<>();
        userAnswers5.add(new UserAnswer(2201,15,false));
        userAnswers5.add(new UserAnswer(2202,15,false));
        userAnswers5.add(new UserAnswer(2203,15,true));
        userAnswers5.add(new UserAnswer(2204,15,true));
        userAnswers5.add(new UserAnswer(2205,15,false));
        UserQuiz userQuiz5=new UserQuiz(22,userAnswers5);
        DB.addUserQuiz(1,22,userQuiz5);

        List<UserAnswer> userAnswers6=new ArrayList<>();
        userAnswers6.add(new UserAnswer(2201,15,true));
        userAnswers6.add(new UserAnswer(2202,15,true));
        userAnswers6.add(new UserAnswer(2203,15,false));
        userAnswers6.add(new UserAnswer(2204,15,true));
        userAnswers6.add(new UserAnswer(2205,15,true));
        UserQuiz userQuiz6=new UserQuiz(22,userAnswers6);
        DB.addUserQuiz(2,22,userQuiz6);
    }
}
