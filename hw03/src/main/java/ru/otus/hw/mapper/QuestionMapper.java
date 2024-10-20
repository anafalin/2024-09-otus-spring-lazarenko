package ru.otus.hw.mapper;

import lombok.experimental.UtilityClass;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.util.List;

@UtilityClass
public class QuestionMapper {
    public List<Question> toQuestionList(List<QuestionDto> dtos) {
        return dtos.stream()
                .map(QuestionMapper::toQuestion)
                .toList();
    }

    private Question toQuestion(QuestionDto dto) {
        return new Question(dto.getText(), dto.getAnswers());
    }
}