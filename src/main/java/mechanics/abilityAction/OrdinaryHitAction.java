package mechanics.abilityaction;

import mechanics.Entity;
import org.jetbrains.annotations.NotNull;
import resource.ServerConfiguration;
import utils.Helper;

/**
 * Created by Андрей on 01.11.2015.
 */
public final class OrdinaryHitAction extends AbilityAction{
    @Override
    public void run(@NotNull Entity entity){
        entity.makeDamage(Helper.randomInt(ServerConfiguration.getInstance().getAbilityMinEffect("OrdinaryHit"),
                                           ServerConfiguration.getInstance().getAbilityMaxEffect("OrdinaryHit")));
    }
}
