package by.praded.dependency_injection.service.impl;

import by.praded.dependency_injection.annotation.Inject;
import by.praded.dependency_injection.dao.QuestionDao;
import by.praded.dependency_injection.service.QuestionService;

/**
 * @author Kiryl Praded
 * 04.02.2021
 */
public class QuestionServiceImpl implements QuestionService {

    private QuestionDao questionDao;

    @Inject
    public QuestionServiceImpl(QuestionDao dao) {
        this.questionDao = dao;
    }
}
