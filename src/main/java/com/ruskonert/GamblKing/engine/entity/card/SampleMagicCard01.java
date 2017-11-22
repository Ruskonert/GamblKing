package com.ruskonert.GamblKing.engine.entity.card;

import com.ruskonert.GamblKing.engine.entity.card.component.Effect;
import com.ruskonert.GamblKing.engine.entity.card.component.Targeting;
import com.ruskonert.GamblKing.engine.entity.card.framework.MagicCard;

public final class SampleMagicCard01 extends MagicCard
{
    public SampleMagicCard01()
    {
        this.setName("샘플 마법 카드");
        this.setDescription("자신 필드에 있는 모든 몬스터의 수비력을 500 감소시키고. 공격력을 500 증가시킵니다.");
        this.setImage("01.jpg");
        // 아래의 함수가 카드를 만들 때 기본으로 호출됩니다.
        //this.setActivateCost(ActivateCost.NONE);
        this.addEffect(Effect.TOTAL_CONTROL_ATTACK_VALUE, Targeting.THIS, 500);
        this.addEffect(Effect.TOTAL_CONTROL_DEFENSE_VALUE, Targeting.THIS, -500);
    }
}
