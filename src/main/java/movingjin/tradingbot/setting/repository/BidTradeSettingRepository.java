package movingjin.tradingbot.setting.repository;

import movingjin.tradingbot.setting.domain.BidTradeSetting;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
    public Optional<BidTradeSetting> findByUserName(String userName) {
        List<BidTradeSetting> result = entityManager.createQuery("select t from Bid_Trade_Setting t where t.USER_NAME = :userName", BidTradeSetting.class)
                .setParameter("userName", userName)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public boolean existsByUserName(String userName) {
        TypedQuery<Boolean> result = entityManager
                .createQuery("select count(e)>0 from Bid_Trade_Setting e where e.USER_NAME = :userName", Boolean.class)
                .setParameter("userName", userName);
        boolean ret = result.getSingleResult();
        return ret;
    }

    @Override
    public Optional<BidTradeSetting> findByUserNameAndCoinName(String userName, String coinName) {
        List<BidTradeSetting> result = entityManager
                .createQuery("select t from Bid_Trade_Setting t where t.USER_NAME = :userName and t.COIN_NAME = :coinName", BidTradeSetting.class)
                .setParameter("userName", userName)
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
