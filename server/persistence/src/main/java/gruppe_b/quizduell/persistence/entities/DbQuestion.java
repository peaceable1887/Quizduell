package gruppe_b.quizduell.persistence.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import gruppe_b.quizduell.domain.entities.Question;

/**
 * Question DbEntity.
 * 
 * @author Christopher Burmeister
 */
@Entity
@Table(name = "questions")
public class DbQuestion extends Question {

    public DbQuestion() {

    }

    public DbQuestion(UUID categoryId, String questionText, String answerOne, String answerTwo, String answerThree,
            String answerFour, int correctAnswer) {
        super(categoryId, questionText, answerOne, answerTwo, answerThree, answerFour, correctAnswer);
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Override
    public UUID getId() {
        return super.getId();
    }

    @Column(name = "categoryId", nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Override
    public UUID getCategoryId() {
        return super.getCategoryId();
    }

    @Column(name = "text", nullable = false)
    @Override
    public String getQuestionText() {
        return super.getQuestionText();
    }

    @Column(name = "answer_one", nullable = false)
    @Override
    public String getAnswerOne() {
        return super.getAnswerOne();
    }

    @Column(name = "answer_two", nullable = false)
    @Override
    public String getAnswerTwo() {
        return super.getAnswerTwo();
    }

    @Column(name = "answer_three", nullable = false)
    @Override
    public String getAnswerThree() {
        return super.getAnswerThree();
    }

    @Column(name = "answer_four", nullable = false)
    @Override
    public String getAnswerFour() {
        return super.getAnswerFour();
    }

    @Column(name = "correct_answer", nullable = false)
    @Override
    public int getCorrectAnswer() {
        return super.getCorrectAnswer();
    }

    public Question createEntity() {
        Question question = new Question();
        question.setId(getId());
        question.setCategoryId(getCategoryId());
        question.setQuestionText(getQuestionText());
        question.setAnswerOne(getAnswerOne());
        question.setAnswerTwo(getAnswerTwo());
        question.setAnswerThree(getAnswerThree());
        question.setAnswerFour(getAnswerFour());
        question.setCorrectAnswer(getCorrectAnswer());

        return question;
    }
}
