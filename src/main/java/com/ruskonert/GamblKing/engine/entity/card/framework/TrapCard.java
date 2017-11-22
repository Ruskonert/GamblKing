package com.ruskonert.GamblKing.engine.entity.card.framework;

import com.ruskonert.GamblKing.engine.entity.card.component.ActivateCost;
import static com.ruskonert.GamblKing.engine.entity.card.component.ActivateCost.NONE;
import com.ruskonert.GamblKing.engine.entity.card.component.CardType;
import com.ruskonert.GamblKing.engine.entity.card.component.Effect;

import java.util.Map;

/**
 * 함정 카드는 먼저 자신 페이지에서 카드를 뒤집어 놓고, 다음 페이지부터 발동할 수 있는 특별한 카드입니다.
 * @see com.ruskonert.GamblKing.engine.entity.card.component.ActivateCost#NONE
 */
public abstract class TrapCard extends CardFramework
{
    public TrapCard() { super(CardType.TARP); }

    public void setActivateCost(ActivateCost activateCost)
    {
        if(activateCost == NONE) try {
            throw new IllegalAccessException("함정 카드는 즉각 발동 조건을 사용할 수 없습니다.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.activateCost = activateCost;
    }

    private ActivateCost activateCost;

    public ActivateCost getActivateCost() {
        return activateCost;
    }

    private Map<Map.Entry<ActivateCost, Effect>, Object[]> effects;
    public Map<Map.Entry<ActivateCost, Effect>, Object[]> getEffects() { return null;}
    public void addEffect(Object... args) { }
}
