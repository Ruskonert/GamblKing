package com.ruskonert.GamblKing.engine.entity.card.component;

/**
 * 카드가 필드에 사용하기 위한 조건부입니다.
 */
public enum ActivateCost
{
    // 아무 카드를 필드에 냈을 때, 발동하는 조건입니다.
    ACTIVATED,

    // 아무 카드가 파괴되었을 때 발동되는 조건입니다.
    DESTORYED,

    // 엔드 페이지일 때, 발동하는 조건입니다.
    END_PAGE,

    // 발동 조건이 없습니다. 필드에 내놓는 순간 즉각 발동됩니다. 이것은 마법 카드에서만 사용이 가능합니다.
    NONE
}
