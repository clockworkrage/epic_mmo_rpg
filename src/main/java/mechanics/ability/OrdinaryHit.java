package mechanics.ability;

import mechanics.abilityaction.AbilityAction;
import mechanics.abilityaction.OrdinaryHitAction;
import resource.ServerConfiguration;

/**
 * Created by Андрей on 01.11.2015.
 */
public final class OrdinaryHit extends Ability implements AttackAbility{

    public OrdinaryHit(){
        cooldown = ServerConfiguration.getInstance().getAbilityCooldown("OrdinaryHit");
        range = ServerConfiguration.getInstance().getAbilityRange("OrdinaryHit");
    }

    @Override
    protected AbilityAction getAction(){
        return new OrdinaryHitAction();
    }
}
