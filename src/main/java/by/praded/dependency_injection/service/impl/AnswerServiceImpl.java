package by.praded.dependency_injection.service.impl;

import by.praded.dependency_injection.annotation.Inject;
import by.praded.dependency_injection.dao.AnswerDao;
import by.praded.dependency_injection.service.AnswerService;

/**
 * @author Kiryl Praded
 * 04.02.2021
 * <p>
 * Add few constructors annotated with {@link Inject}
 * for {@link by.praded.dependency_injection.exception.TooManyConstructorsException}.
 */
public class AnswerServiceImpl implements AnswerService {
    private AnswerDao answerDao;

    @Inject
    public AnswerServiceImpl(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Inject
    public AnswerServiceImpl(AnswerDao answerDao, String str) {
        this.answerDao = answerDao;
    }
}
