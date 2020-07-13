package org.sparkaton.levelup;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sparkaton.levelup.dto.Answer;
import org.sparkaton.levelup.DB.DB;
import org.sparkaton.levelup.dto.Question;
import org.sparkaton.levelup.dto.Quiz;
import org.sparkaton.levelup.quillionz.Qquiz;
import org.sparkaton.levelup.quillionz.Qmcq;
import org.sparkaton.levelup.quillionz.Qquestion;
import org.sparkaton.levelup.quillionz.QtrueFalsePerSentence;
import org.sparkaton.levelup.quillionz.auth.Qauth;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class QuillionzService {
    private static final String[] TRUE_FALSE_ANSWERS = {"True", "False"};

    @SneakyThrows
    public ClientHttpResponse getQuestions() {
        int quizId = DB.getQuizID();
        String title = "Roman History";
        String article = "Roman history has been influential on the modern world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems. Roman history can be divided into the following periods:Pre-historical and early Rome, covering Rome's earliest inhabitants and the legend of its founding by Romulus.The period of Etruscan dominance and the Regal Period, in which according to tradition, Romulus was the first of seven kings.The Roman Republic, which commenced in 509 BC when kings were replaced with rule by elected senators. The period was marked by vast expansion of Roman territory. During the 5th century BC, Rome gained regional dominance in Latium. With the Punic Wars from 264 to 146 BC, Ancient Rome gained dominance over the Western Mediterranean, displacing Carthage as the dominant regional power.The Roman Empire - with the rise of Julius Caesar, the Republic waned, and by all measures concluded after a period of civil war and the victory of Octavian, the adopted son of Caesar in 27 BC over Mark Antony. With the collapse of the Western Roman Empire, Rome's power declined, and it eventually became part of the Eastern Roman Empire, as the Duchy of Rome until the 8th century. At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.[1]Medieval Rome - characterized by a break with Byzantium and the formation of the Papal States. The Papacy struggled to retain influence in the emerging Holy Roman Empire, and during the Saeculum obscurum, the population of Rome fell to as low as 30,000 inhabitants. Following the East–West Schism and the limited success in the Investiture Controversy, the Papacy did gain considerable influence in the High Middle Ages, but with the Avignon Papacy and the Western Schism, the city of Rome was reduced to irrelevance, its population falling below 20,000. Rome's decline into complete irrelevance during the medieval period, with the associated lack of construction activity, assured the survival of very significant ancient Roman material remains in the centre of the city, some abandoned and others continuing in use.The Roman Renaissance - in the 15th century, Rome replaced Florence as the centre of artistic and cultural influence. The Roman Renaissance was cut short abruptly with the devastation of the city in 1527, but the Papacy reasserted itself in the Counter-Reformation, and the city continued to flourish during the early modern period. Rome was annexed by Napoleon and was part of the First French Empire from 1798–1814.Modern history - the period from the 19th century to today. Rome came under siege again after the Allied invasion of Italy and was bombed several times. It was declared an open city on 14 August 1943. Rome became the capital of the Italian Republic (established in 1946). With a population of 4.4 million in its (as of 2015; 2.9 million within city limits), it is the largest city in Italy. It is among the largest urban areas of the European Union and classified as a global city.";
        Qquiz qquiz = getQquiz(title, article);
        log.info(qquiz.toString());
        Quiz quiz = convertQuillionzToQuize(quizId, qquiz, title);
        DB.addQuiz(quizId,quiz);

        return null;
    }


    private Qquiz getQquiz(String title, String article) throws com.fasterxml.jackson.core.JsonProcessingException {
        final String[] responseBody = new String[1];
        String parsedTitle = title.replace(" ", "%20");
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://app.quillionz.com:8243/quillionzapifree/1.0.0/API/SubmitContent_GetQuestions?shortAnswer=false&recall=false&mcq=true&whQuestions=false&title=" + parsedTitle;
        String bearer = getBearer();
        ClientHttpResponse clientHttpResponse = restTemplate.execute(url, HttpMethod.POST, httpRequest -> {
            HttpHeaders headers = httpRequest.getHeaders();
            headers.add("Authorization", "Bearer " + bearer);
            headers.add("Content-Type", "text/plain");
            headers.add("Accept", "*/*");
            OutputStream httpRequestBody = httpRequest.getBody();
            httpRequestBody.write(article.getBytes());
        }, httpResponse -> {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getBody()));
            responseBody[0] = bufferedReader.readLine();
            log.info(responseBody[0]);
            return httpResponse;
        });
//        responseBody[0] = RESPONSE;
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody[0], Qquiz.class);
    }
    public String getBearer(){
        String secret="THdETDh4QXV0TjJyNzhoek9FZ3dTN0pYSFpVYTpLYjN6bEhqNktDNWRzcFF6cld4VlRkeFU3RHNh";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization","Basic "+secret);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type","client_credentials");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Qauth> response =
                restTemplate.exchange("https://app.quillionz.com:8243/token",
                        HttpMethod.POST,
                        entity,
                        Qauth.class);
        return response.getBody().getAccess_token();
    }

    private Quiz convertQuillionzToQuize(int quizId, Qquiz qquiz, String title) {
        int questionSequence = 1;
        List<Question> questions = new ArrayList<>();
        for (Qmcq qmcq : qquiz.getData().getMultipleChoiceQuestions().getMcq()) {
            int questionId = quizId * 100 + questionSequence++;
            questions.add(createQuestionFromMcq(questionId, qmcq));
        }
        for (QtrueFalsePerSentence qtrueFalsePerSentence : qquiz.getData().getMultipleChoiceQuestions().getTrueFalse()) {
            for (Qquestion qquestion : qtrueFalsePerSentence.getQuestionList()) {
                int questionId = quizId * 100 + questionSequence++;
                questions.add(createQuestionFromQquestion(questionId, qquestion));
            }
        }
        return Quiz.builder().quizId(quizId)
                        .title(title)
                        .questions(questions)
                        .build();

    }
    private Question createQuestionFromQquestion(int questionId, Qquestion question) {
        List<Answer> answers = new ArrayList<>();
        int correctAnswerId = 0;
        int index = 1;
        for (String answer : TRUE_FALSE_ANSWERS) {
            int answerId = questionId * 10 + index++;
            if (answer.equals(question.getAnswer())){
                correctAnswerId = answerId;
            }
            answers.add(Answer.builder()
                    .answerId(answerId)
                    .answer(answer).build());
        }
        return Question.builder().questionId(questionId)
                .question(question.getQuestion())
                .answers(answers)
                .correctAnswer(correctAnswerId)
                .build();
    }
    private Question createQuestionFromMcq(int questionId, Qmcq qmcq) {
        List<Answer> answers = new ArrayList<>();
        int correctAnswerId = 0;
        List<String> options = qmcq.getOptions();
        int index = 1;
        for (String answer : qmcq.getOptions()) {
            int answerId = questionId * 10 + index++;
            if (answer.equals(qmcq.getAnswer())){
                correctAnswerId = answerId;
            }
            answers.add(Answer.builder()
                    .answerId(answerId)
                    .answer(answer).build());
        }
        return Question.builder().questionId(questionId)
                .question(qmcq.getQuestion())
                .answers(answers)
                .correctAnswer(correctAnswerId)
                .build();
    }

    public static String RESPONSE = "{\n" +
            "    \"ContentEncoding\": null,\n" +
            "    \"ContentType\": \"application/json\",\n" +
            "    \"Data\": {\n" +
            "        \"multipleChoiceQuestions\": {\n" +
            "            \"mcq\": [\n" +
            "                {\n" +
            "                    \"Question\": \"Rome's decline into complete irrelevance during the medieval period, with the associated lack of construction activity, assured the survival of very significant ancient Roman material remains in the centre of the city, some abandoned and others continuing in use.The Roman Renaissance - in the 15th century, Rome replaced Florence as the centre of artistic and cultural influence. The Roman Renaissance was cut short abruptly with the devastation of the city in _______, but the Papacy reasserted itself in the Counter-Reformation, and the city continued to flourish during the early modern period.\",\n" +
            "                    \"Answer\": \"1527\",\n" +
            "                    \"Options\": [\n" +
            "                        \"1527\",\n" +
            "                        \"1526\",\n" +
            "                        \"1537\",\n" +
            "                        \"1585\",\n" +
            "                        \"1563\",\n" +
            "                        \"1485\",\n" +
            "                        \"1651\",\n" +
            "                        \"1649\",\n" +
            "                        \"1340\",\n" +
            "                        \"1640\"\n" +
            "                    ],\n" +
            "                    \"originalSentence\": \"The Roman Renaissance was cut short abruptly with the devastation of the city in 1527, but the Papacy reasserted itself in the Counter-Reformation, and the city continued to flourish during the early modern period.\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"trueFalse\": [\n" +
            "                {\n" +
            "                    \"questionList\": [\n" +
            "                        {\n" +
            "                            \"question\": \"Roman history has been influential on the ancient world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"Roman history has been influential on the modern world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"Medieval Rome - characterized history has been influential on the modern world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"Roman history has been influential on the modern world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"Roman history has been influential on the modern world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems.\",\n" +
            "                            \"answer\": \"True\",\n" +
            "                            \"correctSent\": \"Roman history has been influential on the modern world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems.\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"originalSentence\": \"Roman history has been influential on the modern world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems.\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"questionList\": [\n" +
            "                        {\n" +
            "                            \"question\": \"The period was marked by vast expansion of Roman territory. At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Medieval Rome - characterized by a break with Byzantium and the formation of Roman Republic.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Medieval Rome - characterized by a break with Byzantium and the formation of the Papal States.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"The period was marked by vast expansion of Roman territory. At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Medieval Rome - characterized by a break with Byzantium and the formation of the Papal States.\",\n" +
            "                            \"answer\": \"True\",\n" +
            "                            \"correctSent\": \"At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Medieval Rome - characterized by a break with Byzantium and the formation of the Papal States.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"The period was marked by vast expansion of Roman territory. At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Roman by a break with Byzantium and the formation of the Papal States.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Medieval Rome - characterized by a break with Byzantium and the formation of the Papal States.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"The period was marked by vast expansion of Roman territory. At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Medieval Rome - characterized by a break with Carthage and the formation of the Papal States.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Medieval Rome - characterized by a break with Byzantium and the formation of the Papal States.\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"originalSentence\": \"At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.Medieval Rome - characterized by a break with Byzantium and the formation of the Papal States.\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"questionList\": [\n" +
            "                        {\n" +
            "                            \"question\": \"Sparta was annexed by Napoleon and was part of the First French Empire from 1798–1814.Modern history - the period from the 19th century to today.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"Rome was annexed by Napoleon and was part of the First French Empire from 1798–1814.Modern history - the period from the 19th century to today.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"Rome was annexed by Napoleon and was part of the First Polish Empire from 1798–1814.Modern history - the period from the 19th century to today.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"Rome was annexed by Napoleon and was part of the First French Empire from 1798–1814.Modern history - the period from the 19th century to today.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"Rome was annexed by Napoleon and was part of the First French Empire from 1798–1814.Modern history - the period from the 19th century to today.\",\n" +
            "                            \"answer\": \"True\",\n" +
            "                            \"correctSent\": \"Rome was annexed by Napoleon and was part of the First French Empire from 1798–1814.Modern history - the period from the 19th century to today.\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"originalSentence\": \"Rome was annexed by Napoleon and was part of the First French Empire from 1798–1814.Modern history - the period from the 19th century to today.\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"questionList\": [\n" +
            "                        {\n" +
            "                            \"question\": \"Rome came under siege again after the Allied invasion of Italy and was bombed several times.\",\n" +
            "                            \"answer\": \"True\",\n" +
            "                            \"correctSent\": \"Rome came under siege again after the Allied invasion of Italy and was bombed several times.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"Carthage came under siege again after the Allied invasion of Italy and was bombed several times.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"Rome came under siege again after the Allied invasion of Italy and was bombed several times.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"Rome came under siege again after the Allied invasion of Poland and was bombed several times.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"Rome came under siege again after the Allied invasion of Italy and was bombed several times.\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"originalSentence\": \"Rome came under siege again after the Allied invasion of Italy and was bombed several times.\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"questionList\": [\n" +
            "                        {\n" +
            "                            \"question\": \"Rome became the capital of the Italian Republic .\",\n" +
            "                            \"answer\": \"True\",\n" +
            "                            \"correctSent\": \"Rome became the capital of the Italian Republic .\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"Carthage became the capital of the Italian Republic .\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"Rome became the capital of the Italian Republic .\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"originalSentence\": \"Rome became the capital of the Italian Republic .\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"questionList\": [\n" +
            "                        {\n" +
            "                            \"question\": \"With a population of 4.4 million in its , it is the largest city in Italy.\",\n" +
            "                            \"answer\": \"True\",\n" +
            "                            \"correctSent\": \"With a population of 4.4 million in its , it is the largest city in Italy.\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"question\": \"With a population of 4.4 million in its , it is the largest city in Germany.\",\n" +
            "                            \"answer\": \"False\",\n" +
            "                            \"correctSent\": \"With a population of 4.4 million in its , it is the largest city in Italy.\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"originalSentence\": \"With a population of 4.4 million in its , it is the largest city in Italy.\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    },\n" +
            "    \"JsonRequestBehavior\": 0,\n" +
            "    \"MaxJsonLength\": 2147483647,\n" +
            "    \"RecursionLimit\": 0\n" +
            "}";
}
