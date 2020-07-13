package org.sparkaton.levelup;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.input.InputMethodTextRun;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sparkaton.levelup.dto.Answer;
import org.sparkaton.levelup.dto.Question;
import org.sparkaton.levelup.dto.Quiz;
import org.sparkaton.levelup.dto.QuizRequest;
import org.sparkaton.levelup.quillionz.QResponse;
import org.sparkaton.levelup.quillionz.Qmcq;
import org.sparkaton.levelup.quillionz.Qquestion;
import org.sparkaton.levelup.quillionz.QtrueFalsePerSentence;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class QuillionzService {
    private static final String[] TRUE_FALSE_ANSWERS = {"True", "False"};
    @SneakyThrows
    public ClientHttpResponse getQuestions() {
        int quizId = 81;
        final String[] responseBody = new String[1];
        String title = "Roman History";
        String parsedTitle = title.replace(" ", "%20");
        RestTemplate restTemplate = new RestTemplate();
        String article = "Roman history has been influential on the modern world, especially in the history of the Catholic Church, and Roman law has influenced many modern legal systems. Roman history can be divided into the following periods:Pre-historical and early Rome, covering Rome's earliest inhabitants and the legend of its founding by Romulus.The period of Etruscan dominance and the Regal Period, in which according to tradition, Romulus was the first of seven kings.The Roman Republic, which commenced in 509 BC when kings were replaced with rule by elected senators. The period was marked by vast expansion of Roman territory. During the 5th century BC, Rome gained regional dominance in Latium. With the Punic Wars from 264 to 146 BC, Ancient Rome gained dominance over the Western Mediterranean, displacing Carthage as the dominant regional power.The Roman Empire - with the rise of Julius Caesar, the Republic waned, and by all measures concluded after a period of civil war and the victory of Octavian, the adopted son of Caesar in 27 BC over Mark Antony. With the collapse of the Western Roman Empire, Rome's power declined, and it eventually became part of the Eastern Roman Empire, as the Duchy of Rome until the 8th century. At this time, the city was reduced to a fraction of its former size, being sacked several times in the 5th to 6th centuries, even temporarily depopulated entirely.[1]Medieval Rome - characterized by a break with Byzantium and the formation of the Papal States. The Papacy struggled to retain influence in the emerging Holy Roman Empire, and during the Saeculum obscurum, the population of Rome fell to as low as 30,000 inhabitants. Following the East–West Schism and the limited success in the Investiture Controversy, the Papacy did gain considerable influence in the High Middle Ages, but with the Avignon Papacy and the Western Schism, the city of Rome was reduced to irrelevance, its population falling below 20,000. Rome's decline into complete irrelevance during the medieval period, with the associated lack of construction activity, assured the survival of very significant ancient Roman material remains in the centre of the city, some abandoned and others continuing in use.The Roman Renaissance - in the 15th century, Rome replaced Florence as the centre of artistic and cultural influence. The Roman Renaissance was cut short abruptly with the devastation of the city in 1527, but the Papacy reasserted itself in the Counter-Reformation, and the city continued to flourish during the early modern period. Rome was annexed by Napoleon and was part of the First French Empire from 1798–1814.Modern history - the period from the 19th century to today. Rome came under siege again after the Allied invasion of Italy and was bombed several times. It was declared an open city on 14 August 1943. Rome became the capital of the Italian Republic (established in 1946). With a population of 4.4 million in its (as of 2015; 2.9 million within city limits), it is the largest city in Italy. It is among the largest urban areas of the European Union and classified as a global city.";
        String bearer = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik5UWmhOMkZpT1dVME1UWXhOalE0TkdFMFptRmxNVFV3TmpneVlqVXpNekUwWlRFMll6UTROZz09In0.eyJzdWIiOiJsZXZlbFVwQGNhcmJvbi5zdXBlciIsImJhY2tlbmRKd3QiOiJleUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpTVXpJMU5pSXNJbmcxZENJNklrNVVXbWhPTWtacFQxZFZNRTFVV1hoT2FsRTBUa2RGTUZwdFJteE5WRlYzVG1wbmVWbHFWWHBOZWtVd1dsUkZNbGw2VVRST1p6MDlJbjA9LmV5Sm9kSFJ3T2x3dlhDOTNjMjh5TG05eVoxd3ZZMnhoYVcxelhDOWhjSEJzYVdOaGRHbHZiblJwWlhJaU9pSlZibXhwYldsMFpXUWlMQ0pvZEhSd09sd3ZYQzkzYzI4eUxtOXlaMXd2WTJ4aGFXMXpYQzl6ZFdKelkzSnBZbVZ5SWpvaWJHVjJaV3hWY0NJc0ltaDBkSEE2WEM5Y0wzZHpiekl1YjNKblhDOWpiR0ZwYlhOY0wydGxlWFI1Y0dVaU9pSlFVazlFVlVOVVNVOU9JaXdpYVhOeklqb2lkM052TWk1dmNtZGNMM0J5YjJSMVkzUnpYQzloYlNJc0ltaDBkSEE2WEM5Y0wzZHpiekl1YjNKblhDOWpiR0ZwYlhOY0wyRndjR3hwWTJGMGFXOXVibUZ0WlNJNklteGxkbVZzVlhBeUlpd2lhSFIwY0RwY0wxd3ZkM052TWk1dmNtZGNMMk5zWVdsdGMxd3ZaVzVrZFhObGNpSTZJbXhsZG1Wc1ZYQkFZMkZ5WW05dUxuTjFjR1Z5SWl3aWFIUjBjRHBjTDF3dmQzTnZNaTV2Y21kY0wyTnNZV2x0YzF3dlpXNWtkWE5sY2xSbGJtRnVkRWxrSWpvaUxURXlNelFpTENKbGVIQWlPakUxT1RRMk16RXlOamNzSW1oMGRIQTZYQzljTDNkemJ6SXViM0puWEM5amJHRnBiWE5jTDJGd2NHeHBZMkYwYVc5dWFXUWlPaUl6TlRNaUxDSm9kSFJ3T2x3dlhDOTNjMjh5TG05eVoxd3ZZMnhoYVcxelhDOWhjSEJzYVdOaGRHbHZibFZWU1dRaU9pSXhZamd4WVRaaE5TMHhNRGxsTFRRMU16RXRZbUptWVMwM1pHRXlNelJoWkdaaU16QWlmUT09Lk5zNURHU3VOSHZDU2UtaWRGRl94dkQ3ODFLb1ZfQ2dTNTdxbDJZcW1SeGpLMVBUa1dRQ0xXTURuZG1rdmFpNmgzbXoxRnkwYXNkamRHOG51TTIxUjN2eG1XOVNhejVsSXQzYUlUT2ZnX2lGUkpmTDBqUVh1TWR1dERkOVBqdjJqWXVPVXcwdW1EazBYeXE4Q1hIUnV1QmZpc2VIYV9Jd0haQ3NXc3lwb3RqNEtSZC1hSmMyVlhIbkdzLU5kQ2dZNG1ZWU1tX3Q1ckZPTTBaQXRHTjVzYXc3SElFa1dFOGtHUW44Z05BcWRuVktiYVByTG9QaC15QVFEX2p3WWdKU0thQUkxalJmZjY2d2lzejZmWHk1U1h3RjQzeXM1Sm9acmtwc0cxVllKbWt0MVNWeXpUM2hMMnJqUTM1ZDNvZUYyTXhjb0JpU2xiZEthS0ppOUxCWU9EZz09IiwiaXNzIjoiaHR0cHM6XC9cL2FwcC5xdWlsbGlvbnouY29tOjk0NDNcL29hdXRoMlwvdG9rZW4iLCJ0aWVySW5mbyI6eyJGcmVlVGllciI6eyJzdG9wT25RdW90YVJlYWNoIjp0cnVlLCJzcGlrZUFycmVzdExpbWl0IjoyLCJzcGlrZUFycmVzdFVuaXQiOiJtaW4ifX0sImtleXR5cGUiOiJQUk9EVUNUSU9OIiwic3Vic2NyaWJlZEFQSXMiOlt7InN1YnNjcmliZXJUZW5hbnREb21haW4iOiJjYXJib24uc3VwZXIiLCJuYW1lIjoiUXVpbGxpb256QVBJLUZyZWUiLCJjb250ZXh0IjoiXC9xdWlsbGlvbnphcGlmcmVlXC8xLjAuMCIsInB1Ymxpc2hlciI6ImFkbWluIiwidmVyc2lvbiI6IjEuMC4wIiwic3Vic2NyaXB0aW9uVGllciI6IkZyZWVUaWVyIn1dLCJhdWQiOiJodHRwOlwvXC9vcmcud3NvMi5hcGltZ3RcL2dhdGV3YXkiLCJhcHBsaWNhdGlvbiI6eyJvd25lciI6ImxldmVsVXAiLCJ0aWVyIjoiVW5saW1pdGVkIiwibmFtZSI6ImxldmVsVXAyIiwiaWQiOjM1MywidXVpZCI6bnVsbH0sInNjb3BlIjoiYW1fYXBwbGljYXRpb25fc2NvcGUgZGVmYXVsdCIsImNvbnN1bWVyS2V5IjoiMFBkWFNXSFVTRlRjSWZZemNQX2Y4cXhfRHZVYSIsImV4cCI6MTU5NDYzMzk2OCwiaWF0IjoxNTk0NjMwMzY4LCJqdGkiOiJhYTJjMzM2ZC1lZDdmLTQ3ZmMtODc2Mi0wOTAyZmJhZDMzZmYifQ.NC81z6iax-jrOk-v4IC_1qg8nMUAFN2urxf_QAj-NBU9GSRbkeCnsvrMZvJujRRO41gcmeFdhLqcFoRTF6oZ6OyHAW2r_AWj44S-IYR0Bhg-QK_xh6Msl_TInC6OlSWjNAYa_R3UAcPojcYbN000Imek4PjKh2T81ZtUSuaSa7xMu8nkIG9ghsuWsbxcDFFf3fQ5CHGSt721cV93i2Mm1xA4d-tA2S7Ip4JhkNXooL9dMfnnFVreX5drKH3RgBpqhK3-UBmsx5U2v0giOWlvBxcc5mqVIX5RcdjpnxW0Vxc85-BoXNasoQ49IThAS2fniIij6i3-k07DTXL_35rIrw";
        String url = "https://app.quillionz.com:8243/quillionzapifree/1.0.0/API/SubmitContent_GetQuestions?shortAnswer=false&recall=false&mcq=true&whQuestions=false&title=" + parsedTitle;
//        ClientHttpResponse clientHttpResponse = restTemplate.execute(url, HttpMethod.POST, httpRequest -> {
//            HttpHeaders headers = httpRequest.getHeaders();
//            headers.add("Authorization", "Bearer " + bearer);
//            headers.add("Content-Type", "text/plain");
//            headers.add("Accept", "*/*");
//            OutputStream httpRequestBody = httpRequest.getBody();
//            httpRequestBody.write(article.getBytes());
//        }, httpResponse -> {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getBody()));
//            responseBody[0] = bufferedReader.readLine();
//            log.info(responseBody[0]);
//            return httpResponse;
//        });
        responseBody[0] = RESPONSE;
        ObjectMapper objectMapper = new ObjectMapper();
        QResponse qResponse = objectMapper.readValue(responseBody[0], QResponse.class);
        log.info(qResponse.toString());
        Quiz quiz = convertQuillionzToQuize(quizId, qResponse, title);
        return null;
    }

    private Quiz convertQuillionzToQuize(int quizId, QResponse qResponse, String title) {
        int questionSequence = 1;
        List<Question> questions = new ArrayList<>();
        for (Qmcq qmcq : qResponse.getData().getMultipleChoiceQuestions().getMcq()) {
            int questionId = quizId * 100 + questionSequence++;
            questions.add(createQuestionFromMcq(questionId, qmcq));
        }
        for (QtrueFalsePerSentence qtrueFalsePerSentence : qResponse.getData().getMultipleChoiceQuestions().getTrueFalse()) {
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

    public void createQuiz(QuizRequest quizRequest) {
        String title = quizRequest.getTitle();
        String article = quizRequest.getArticle();


    }
}
