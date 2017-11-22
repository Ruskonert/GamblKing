package com.ruskonert.GamblKing.engine.entity.card.framework;

import com.ruskonert.GamblKing.engine.entity.card.component.ActivateCost;
import com.ruskonert.GamblKing.engine.entity.card.component.CardType;
import com.ruskonert.GamblKing.engine.entity.card.component.Effect;

import java.util.Map;

/**
 * 카드를 뒤집어 놓고 다음 자기 페이지에서 발동이 가능한 함정 카드과 다르게 마법 카드는
 * 바로 발동할 수 있는 특징이 있습니다. 그래서 만든 사람이 마법 카드 발동 조건을 특별하게
 * 만들지 않는 이상, 마법 카드는 필드에 내는 순간 바로 발동됩니다.
 * 해당 클래스 안에 내포되어 있는 {@link #addEffect(Object...)}을
 * 사용해 마법 카드를 필드에 냈을 때, 바로 발동되는 효과들을 첨부할 수 있습니다.
 *
 * @see com.ruskonert.GamblKing.engine.entity.card.component.ActivateCost#NONE
 */
public abstract class MagicCard extends CardFramework
{
    public MagicCard()
    {
        super(CardType.MAGIC);
    }

    private ActivateCost activateCost = ActivateCost.NONE;

    /**
     * 해당 카드가 발동해야 할 조건입니다. 임의적으로 지정해주지 않으면
     * 기본 발동 조건은 전혀 없습니다.
     * @param activateCost
     */
    public void setActivateCost(ActivateCost activateCost) {
        this.activateCost = activateCost;
    }

    /**
     * 발동 조건을 가져옵니다.
     * @return
     */
    public ActivateCost getActivateCost() {
        return activateCost;
    }

    private Map<Map.Entry<ActivateCost, Effect>, Object[]> effects;
    public Map<Map.Entry<ActivateCost, Effect>, Object[]> getEffects() { return null;}

    /**
     * 마법 카드의 효과를 설정합니다.
     * 만약 ActivateCost가 NONE이라면, 발동 조건이 없어 해당 카드를 필드에 내놓는 순간 바로 발동될 것입니다.
     * 만약 이 효과가 없을 경우 그 마법 카드는 함정 카드처럼 바로 사용할 수 없습니다.
     * @param args
     */
    public void addEffect(Object... args) { /* Complied code */ }
}
