package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.BidTradeSetting;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class BidTradeSettingRepository implements BidTradeSettingInterface {
    private final EntityManager entityManager;

    public BidTradeSettingRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BidTradeSetting save(BidTradeSetting bidTradeSetting) {
        entityManager.persist(bidTradeSetting);
        return bidTradeSetting;
    }

    @Override
    public Optional<BidTradeSetting> findByCoinName(String coinName) {
        List<BidTradeSetting> result = entityManager.createQuery("select t from Bid_Trade_Setting t where t.COIN_NAME = :coinName", BidTradeSetting.class)
                .setParameter("coinName", coinName)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<BidTradeSetting> findAll() {
        List<BidTradeSetting> result = entityManager.createQuery("select t from Bid_Trade_Setting t", BidTradeSetting.class).getResultList();
        return result;
    }
}
