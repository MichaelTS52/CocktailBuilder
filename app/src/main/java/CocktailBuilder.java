import android.app.Application;

import com.mts.cocktailbuilder.stateMangement.BarState;

public class CocktailBuilder extends Application {

    private BarState barStateManager = new BarState();

    public BarState getBarStateManager() {
        return barStateManager;
    }

}
