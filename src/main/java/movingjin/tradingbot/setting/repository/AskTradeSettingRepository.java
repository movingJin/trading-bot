package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.AskTradeSetting;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class AskTradeSettingRepository implements AskTradeSettingInterface {
    private final EntityManager entityManager;

    public AskTradeSettingRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AskTradeSetting save(AskTradeSetting askTradeSetting) {
        entityManager.persist(askTradeSetting);
        return askTradeSetting;
    }

    @Override
    public Optional<AskTradeSetting> findByCoinName(String coinName) {
        List<AskTradeSetting> result = entityManager.createQuery("select t from Ask_Trade_Setting t where t.COIN_NAME = :coinName", AskTradeSetting.class)
                .setParameter("coinName", coinName)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<AskTradeSetting> findAll() {
        List<AskTradeSetting> result = entityManager.createQuery("select t from Ask_Trade_Setting t", AskTradeSetting.class).getResultList();
        return result;
    }
}
