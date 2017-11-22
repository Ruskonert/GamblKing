package com.ruskonert.GamblKing.engine.entity.card;

import com.ruskonert.GamblKing.engine.entity.card.component.ActivateCost;
import com.ruskonert.GamblKing.engine.entity.card.component.Effect;
import com.ruskonert.GamblKing.engine.entity.card.component.Targeting;
import com.ruskonert.GamblKing.engine.entity.card.framework.TrapCard;

public class SampleTrapCard01 extends TrapCard
{
    public SampleTrapCard01()
    {
        this.setName("샘플 함정 카드");
        this.setDescription("아무 카드를 발동했을 때, 필드 위에 카드 1장을 파괴하고, 상대 플레이어에게 8000 데미지를 줍니다.");
        this.setImage("00.jpg");

        this.setActivateCost(ActivateCost.ACTIVATED);

        // effect를 여러개 넣을 수 있습니다.
        // 1장만 해당되는 경우에는 뒤에 숫자를 적지 않아도 됩니다.
        this.addEffect(Effect.DESTORY_CARD);

        // addEffrect() 안에는 다음 순서대로 넣어주시면 됩니다.
        // [Effect], [targeting], [value], [Effect], [targeting], [value]...
        // 여기서 targeting은 필수가 아닙니다.
        this.addEffect(Effect.PLAYER_DAMAGE, Targeting.OTHER, 8000);
    }
}