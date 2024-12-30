package com.mts.cocktailbuilder.configuration;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mts.cocktailbuilder.configuration.CocktailBuilderDatabase.*;
import com.mts.cocktailbuilder.entity.Recipe;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

public class CocktailBuilderDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cocktailBuilder.db";
    public static final int DATABASE_VERSION = 1;

    public CocktailBuilderDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " +
                RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeEntry.COLUMN_NAME + " TEXT NOT NULL," +
                RecipeEntry.COLUMN_METHOD + " TEXT, " +
                RecipeEntry.COLUMN_RATING + " REAL DEFAULT 0, " +
                RecipeEntry.COLUMN_NOTES + " TEXT" +
                ");";

        final String SQL_CREATE_DRINKS_TABLE = "CREATE TABLE " +
                DrinkEntry.TABLE_NAME + " (" +
                DrinkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DrinkEntry.COLUMN_BRAND + " TEXT, " +
                DrinkEntry.COLUMN_VARIANT + " TEXT, " +
                DrinkEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                DrinkEntry.COLUMN_INBAR + " INTEGER DEFAULT 1," +
                DrinkEntry.COLUMN_COMMON + " INTEGER DEFAULT 0" +
                ");";

        final String SQL_CREATE_RELATIONAL_TABLE = "CREATE TABLE " +
                IngredientRelation.TABLE_NAME + " (" +
                IngredientRelation.COLUMN_INGREDIENTID + " INTEGER, " +
                IngredientRelation.COLUMN_RECIPEID + " INTEGER, " +
                IngredientRelation.COLUMN_AMOUNT + " TEXT NOT NULL, " +
                IngredientRelation.COLUMN_SUBSTITUTE + " INTEGER, " +
                "FOREIGN KEY(" + IngredientRelation.COLUMN_INGREDIENTID + ")" +
                " REFERENCES " + DrinkEntry.TABLE_NAME + "(" + DrinkEntry._ID +"), " +
                "FOREIGN KEY(" + IngredientRelation.COLUMN_RECIPEID + ")" +
                " REFERENCES " + RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID +"), " +
                "UNIQUE(" + IngredientRelation.COLUMN_RECIPEID + ", " + IngredientRelation.COLUMN_INGREDIENTID + ")" +
                ");";

        db.execSQL(SQL_CREATE_RECIPE_TABLE);
        db.execSQL(SQL_CREATE_DRINKS_TABLE);
        db.execSQL(SQL_CREATE_RELATIONAL_TABLE);

//        importBaseData(db);
    }

//    public void importBaseData(SQLiteDatabase db) {
//
//        //// Do base ingredient data
//        String defaultIngredients =
//                "INSERT INTO drinksList VALUES" +
//                "(1,'captain morgan''s','spiced','rum',1,0)," +
//                "(2,'coca','','cola',0,0)," +
//                "(3,'','cranberry','juice',1,0)," +
//                "(4,'smirnoff','','vodka',1,0)," +
//                "(5,'','london dry','gin',0,0)," +
//                "(6,'','blanco','tequila',0,0)," +
//                "(7,'','peach','schnapps',0,0)," +
//                "(8,'','','lemonade',1,0)," +
//                "(9,'kraken','spiced','rum',1,0)," +
//                "(11,'rumbullion','spiced','rum',1,0)," +
//                "(12,'','parma violet','gin',1,0)," +
//                "(13,'fee brothers','angostura (orange)','bitters',1,0)," +
//                "(14,'fee brothers','plumb','bitters',1,0)," +
//                "(15,'','strawberry','gin',1,0)," +
//                "(16,'jack daniel''s','tennessee honey','bourbon',0,0)," +
//                "(17,'','sugar','syrup',1,1)," +
//                "(19,'','','egg white',0,1)," +
//                "(20,'','orange','juice',1,0)," +
//                "(21,'','cranberry','juice',0,0)," +
//                "(22,'','pineapple','juice',1,0)," +
//                "(23,'','lime','juice',1,0)," +
//                "(24,'','lemon','juice',1,0)," +
//                "(25,'','grenadine','syrup',1,0)," +
//                "(26,'','spiced','rum',1,0)," +
//                "(27,'','dark','rum',1,0)," +
//                "(28,'','white','rum',1,0)," +
//                "(29,'','pineapple','fruit',0,1)," +
//                "(30,'','coconut','rum',1,0)," +
//                "(31,'','coconut','cream',0,0)," +
//                "(32,'','','sugar',1,1)," +
//                "(33,'','amaretto','liqueur',1,0)," +
//                "(34,'','vinnilla','vodka',0,0)," +
//                "(35,'','chocolate','liqueur',0,0)," +
//                "(36,'','scotch','whiskey',0,0)," +
//                "(37,'','rye','whiskey',0,0)," +
//                "(38,'jack daniel''s','honey','whiskey (burbon)',0,0)," +
//                "(39,'bailey''s','cream','liqueur',1,0)," +
//                "(40,'','','whiskey',1,0)," +
//                "(41,'','triple sec','liqueur',0,0)," +
//                "(42,'','coffee','liqueur',0,0)," +
//                "(43,'','curacao','liqueur',0,0)," +
//                "(44,'','prosecco','wine',0,0)," +
//                "(45,'','lemon','sorbet',0,0)," +
//                "(47,'','rosemary','herb',0,1)," +
//                "(50,'','vermoth','wine',0,0)," +
//                "(58,'','sweet','vermouth',0,0)," +
//                "(59,'','peychauds (licorice)','bitters',0,0)," +
//                "(60,'','campari','liqueur',0,0)," +
//                "(61,'','ginger','juice',0,0)," +
//                "(62,'','ginger','ale',0,0)," +
//                "(63,'','','soda',1,0)," +
//                "(64,'','grapefruit','juice',0,0)," +
//                "(65,'','benedictine','liqueur',0,0)," +
//                "(66,'','mint','leaves',0,1)," +
//                "(67,'','','bourbon',0,0)," +
//                "(68,'','old town','gin',0,0)," +
//                "(69,'','heavy','cream',0,1)," +
//                "(70,'','berries','fruit',0,0)," +
//                "(74,'','','salt',0,1)," +
//                "(75,'','','water',1,1)," +
//                "(76,'','black','pepper',0,1)," +
//                "(77,'','','honey',0,1)," +
//                "(78,'','coriander','herb',0,1)," +
//                "(79,'','apricot','brandy',0,0)," +
//                "(80,'','','champagne',0,0)," +
//                "(81,'','','brandy',0,0)," +
//                "(82,'','apple','brandy',0,0)," +
//                "(83,'','pisco','brandy',0,0)," +
//                "(84,'disorono','velvet','liqueur',0,0)," +
//                "(85,'','creme de cacao','liqueur',0,0)," +
//                "(86,'','espresso','coffee',0,1)," +
//                "(87,'','passoa','liqueur',0,0)," +
//                "(88,'','raspberry','fruit',0,0)," +
//                "(89,'','semi','milk',0,0)," +
//                "(90,'','','milk',0,1)," +
//                "(91,'','','ice',0,1)," +
//                "(92,'','white chocolate','liqueur',0,0)," +
//                "(93,'','','cointreau',0,0)," +
//                "(94,'','','cognac',0,0)," +
//                "(95,'','cherry','liqueur',0,0)," +
//                "(96,'','creme de cassis','liqueur',0,0)," +
//                "(97,'','almond','milk',0,0)," +
//                "(98,'','chambord','liqueur',0,0)," +
//                "(99,'','cucumber','veg',0,1)," +
//                "(100,'','rose','water',1,0)," +
//                "(101,'licor 43','vanilla','liqueur',0,0)," +
//                "(102,'','advocaat','liqueur',0,0)," +
//                "(103,'','basil','leaf',1,1)," +
//                "(104,'','orgeat (almond)','syrup',1,0)," +
//                "(105,'','respasado','tequila',1,0)," +
//                "(106,'mr black','coffee','liqueur',1,0)," +
//                "(107,'','creme de mure','liqueur',0,0)," +
//                "(108,'','lychee','juice',1,0);";
//
//        String defaultRecipeList = "INSERT INTO recipeList VALUES" +
//        "(4,'vodka cranberry','shake and serve. over ice if preferred',0.0,'')," +
//        "(5,'hole in one','float orange after mixing',0.0,'')," +
//        "(6,'burnso tropical','shake all together',0.0,'')," +
//        "(10,'dessert sunrise','half as needed',0.0,NULL)," +
//        "(11,'lemony snicket','shake all but grenadine. add splash and stir briefly',0.0,'quite fresh easy on the lemon. Made better by a dash or amoretto')," +
//        "(12,'loose caboose','shake 1-3. top with lemonade.',0.0,NULL)," +
//        "(13,'pine orgy','shake and serve',0.0,NULL)," +
//        "(20,'sex on the beach','shake',0.0,'')," +
//        "(21,'gin&sin','2 splashes of grenadine all shaken',0.0,NULL)," +
//        "(22,'ershammar dream','who knows just shake',0.0,NULL)," +
//        "(23,'georgia gin','probs shake',0.0,NULL)," +
//        "(24,'gin citric',replace('add grenadine last, or don''t i can''t tell you what to do\n\nshake or stir depending on qaulity of gin. top with extra orange depending on glass used','\n',char(10)),0.0,'')," +
//        "(25,'bailey''s madras','shake all bar cranberry. fill with cranberry',0.0,NULL)," +
//        "(26,'bahia breeze','build over ice garnish with lime',0.0,NULL)," +
//        "(27,'cabo','go light on lime. shake and strain',0.0,NULL)," +
//        "(28,'mexican punch','',0.0,NULL)," +
//        "(29,'loco lemonade','half recipe. build over ice. float lemon juice',0.0,NULL)," +
//        "(30,'mexicana',replace('small volume double cocktail \nshake all bar grenadine','\n',char(10)),0.0,'')," +
//        "(31,'shaker','it''s in the name',0.0,NULL)," +
//        "(32,'cinco de mayo','half this. 👀 shake with lots of ice',4.0,'')," +
//        "(33,'baby aspirine of humbolt','shake shake shake shake shake it',0.0,'')," +
//        "(34,'michael''s downfall','build or shake. 2 splashes grenadine, equal parts orange and cranberry to fill',0.0,'try kraken next time not rumbullion')," +
//        "(35,'bat bite','build over ice. full with cranberry',0.0,NULL)," +
//        "(36,'beaver shot','shake',0.0,'')," +
//        "(37,'bahama mama sunrise','',0.0,NULL)," +
//        "(38,'rum zombie','',1.5,'')," +
//        "(39,'pina colada','add all and blend',5.0,'')," +
//        "(40,'amaretto sour','heavy on lime if you like it sour. less if you like it sweet',5.0,NULL)," +
//        "(41,'rock bottom',replace('mix all except tequila, add slowly \n\ntry at your own peral','\n',char(10)),0.0,NULL)," +
//        "(42,'bronze monkey','shake it',3.0,'')," +
//        "(43,'force 12','shake',0.0,NULL)," +
//        "(44,'orange flux','',0.0,NULL)," +
//        "(45,'plasse knock out','top with orange. shake',0.0,NULL)," +
//        "(46,'kingston','',2.0,'little bit heavier on lime')," +
//        "(47,'excuse me','top with lemonade after shake',0.0,'')," +
//        "(48,'nutty dream','',0.0,NULL)," +
//        "(49,'sax with t','top with equal pinapple and orange. 1-2 parts amaretto',5.0,'')," +
//        "(50,'scottalian','shake all  add grenadine last ( can half)',3.0,'')," +
//        "(51,'snikertini','sub choc liqueur with second part bailey s',5.0,'better with choc Bailey''s')," +
//        "(52,'whiskey sour','shake hard and fast 20s strain',4.0,NULL)," +
//        "(53,'screaming o','dry shake first top with whipped cream creme de cacao and hazelnut syrup',3.5,'')," +
//        "(54,'irish american','coconut rum optional',5.0,NULL)," +
//        "(55,'jamaican sunrise','float cranberry',0.0,'')," +
//        "(56,'blue hawaiian','add all and shake for radio active green',3.0,'')," +
//        "(57,'sgroppino','scoop of lemon sorbet mix by hand to avoid over melting',0.0,'')," +
//        "(59,'dirty dianne','',2.5,NULL)," +
//        "(60,'old fashioned',replace('large ice and stir\n\ncan sub rye for bourbon \n\ntypically use sugar cube instead of syrup','\n',char(10)),4.0,'')," +
//        "(61,'manhattan',replace('stirred not shaken 30s \n\nadd luxardo cherry if possible','\n',char(10)),0.0,NULL)," +
//        "(62,'sazerac','stirred',0.0,NULL)," +
//        "(63,'whisky fix',replace('can sub bourbon for rye\n\nwhip (single ice dissolved)\n\ncrushed ice if possible','\n',char(10)),3.5,NULL)," +
//        "(64,'boulevardier',replace('briefly stir\n\ngarnish with orange twist','\n',char(10)),3.5,replace('some recipes call for equal parts \n\nbetter as small volume. 3 is a lot','\n',char(10)))," +
//        "(65,'presbyterian',replace('soda to ale ratio to desired taste\n\ncan sub ale for fresh ginger juice\n\nwhip lime and rye and build over ice','\n',char(10)),0.0,'')," +
//        "(66,'blinker','shake',4.0,'remember to shake grapefruit juice / add some passoa')," +
//        "(67,'monte carlo',replace('2- 3 parts rye\n\ngood stir\n\ngarnish with lemon twist','\n',char(10)),0.0,NULL)," +
//        "(68,'mint julep',replace('2+ of bourbon\nmuddle in glass \n\noptional garnish with rum','\n',char(10)),0.0,NULL)," +
//        "(69,'martini',replace('stir with ice \n\ngarnish with olive or lemon twist','\n',char(10)),0.0,NULL)," +
//        "(70,'martínez','garnish with lemon twist',0.0,NULL)," +
//        "(71,'gimlet','shake',4.0,NULL)," +
//        "(72,'negroni',replace('orange twist garnish\n\nbuild over ice','\n',char(10)),0.0,NULL)," +
//        "(73,'tom collins',replace('whip \n\ntop with soda','\n',char(10)),0.0,NULL)," +
//        "(75,'bramble',replace('dry shake without gin\n\nadd to crushed ice and stir','\n',char(10)),0.0,NULL)," +
//        "(76,'spiked','blend with cup of ice',0.0,'')," +
//        "(77,'senku','can use soda or lemonade. adjust honey and lemon if using lemonade.',0.0,NULL)," +
//        "(78,'daiquiri','go light on lime or heavy on sugar to taste',0.0,NULL)," +
//        "(79,'americano',replace('build in glass \n\ngarnish with orange \n\nlemonade or soda depending on preference','\n',char(10)),4.0,'')," +
//        "(80,'margaritas',replace('up lime / optional sugar syrup\n\nshake and rim glass with lime+flakey salt','\n',char(10)),0.0,NULL)," +
//        "(81,'mexican firing squad','shake  can top with soda  and orange twist',3.5,replace('solid tequila cocktail similar to margarita\n\ncould be sweater?','\n',char(10)))," +
//        "(82,'mai tai',replace('whip serve on crushed ice with mint\n\noptional lime juice','\n',char(10)),4.0,replace('only got such a high rating with coconut rum\n\ncomes out a beautiful emerald with rumbullion\ncould use a touch of sweetness','\n',char(10)))," +
//        "(83,'mojito','dry shake and  over  crushed ice',0.0,NULL)," +
//        "(84,'hotel national special','top with bitters',0.0,NULL)," +
//        "(85,'french 75','shake all but champagne use to top',3.5,NULL)," +
//        "(86,'pink lady','whip into coup bitters to top',5.0,replace('whip gently\n\nif doubling 1 med-large egg is enough','\n',char(10)))," +
//        "(87,'jack rose','',0.0,NULL)," +
//        "(88,'champagne cocktail',replace('drop sugar cube to bottom of glass\ngenerously coat in bitters\nchampagne to fill','\n',char(10)),0.0,NULL)," +
//        "(89,'pisco sour',replace('shake well strain into coup\ntop with orange bitters or cinnamon','\n',char(10)),0.0,NULL)," +
//        "(90,'long island iced tea',replace('mix everything but cola in shaker use coke to top\n\ncan go heavier on lime','\n',char(10)),0.0,NULL)," +
//        "(91,'amf',replace('shake all bar lemonade use to fill  \n\ncan go heavier on lime','\n',char(10)),3.0,'try with better lemonade')," +
//        "(92,'cosmopolitan','shake all',2.0,'very strong vodka tasting cocktail. try upping  lemon a tad or adding 0.5 of sugar')," +
//        "(93,'tropical pink  missy','whip',4.0,NULL)," +
//        "(94,'mexican  sunset','',3.0,'little bit too pinapple, still nice maybe add some lime?')," +
//        "(95,'nutty mudslide','dry shake then wet then strain over ice',3.5,NULL)," +
//        "(96,'nutty mudslide #2','dry then wet then strain',0.0,NULL)," +
//        "(97,'mudslide','',0.0,NULL)," +
//        "(98,'sweet mudslide','can up vanilla vodka (1.5) if low alcohol',4.5,'')," +
//        "(99,'sex with the captain','build over ice use equal cranberry and orange to fill',3.5,NULL)," +
//        "(100,'espresso martini',replace('optional 5ml sugar syrup. \ncan play with ratios all in cup and shake','\n',char(10)),0.0,NULL)," +
//        "(101,'espresso martini #2',replace('shake and double strain.\n\ntry with vanilla vodka !','\n',char(10)),0.0,NULL)," +
//        "(102,'ghost goblet',replace('cointreau and white cranberry recommend \n\nshake and strain into martini glass','\n',char(10)),4.0,'essentially a vodka cranberry with an orange twist. quite strong')," +
//        "(103,'punch in the head',replace('shake into large tiki glass\n\noptional: garnish orange/cherry','\n',char(10)),0.0,NULL)," +
//        "(104,'baby vox','stirred not shaken 👀',4.0,'grenadine and or amaretto')," +
//        "(105,'big red hoot','build over colin''s glass fill with pineapple',3.0,NULL)," +
//        "(106,'cowgirl quencher','build over crushed ice add grenadine at the end',4.5,'not very fancy but nice change in flavour as you drink')," +
//        "(107,'mexican prison cocktail','',0.0,NULL)," +
//        "(108,'fubar kool-aid','',4.0,'try with dark rum or remove')," +
//        "(109,'red headed woodpecker','shake into collins cranberry to fill and turn drink red',5.0,'splash of grenadine nice add. not very alcoholic but very nice')," +
//        "(110,'passoa bramble','shake all but grenadine',3.0,'light citrus refreshing. go light on lemon/heavy on sugar')," +
//        "(111,'pink hawaiian passion','shake for nice pinapple head',4.0,NULL)," +
//        "(112,'raspberry mandarin',replace('blend rasberries grenadine and sugar syrup pinch salt and a dash of lime \n\noptional passoa doesn''t add much\n\nshake with rest and double strain','\n',char(10)),5.0,NULL)," +
//        "(113,'almond joy','',0.0,NULL)," +
//        "(114,'almond joy #2','',5.0,NULL)," +
//        "(115,'terry''s martini','',0.0,NULL)," +
//        "(116,'tiramisú','shake and strain into coup or other small volume',4.0,NULL)," +
//        "(117,'chocolate lady','whip ? small volume',2.5,'sweet and sour, like lemon sour')," +
//        "(118,'crooked sisters','shake into small volume',3.5,'very similar to Terry''s')," +
//        "(119,'ramos gin fizz','dry shake then whip with descent ice top with soda',3.5,'')," +
//        "(120,'french 75 #2','fill about half with champagne or other sparkling wine . shake the rest and pour over champagne',0.0,NULL)," +
//        "(121,'raspberry sniket','add half of grenadine to shake and half after',5.0,NULL)," +
//        "(122,'florida rain','shake and strain  into collins or half and into coup',4.0,NULL)," +
//        "(123,'high risk society','shake throughly cold and double strain into coup',4.5,replace('sweet and sour with a nice hit of cherry\n\ncareful not to over dilute','\n',char(10)))," +
//        "(124,'naked pink (a barbie)','add all and shake with large ice vigorously  double strain',4.5,'comes out pale frothy pink. does 2 coupes')," +
//        "(125,'cherry cobbler','shake and strain',3.0,NULL)," +
//        "(126,'captain''s french kiss','shake all but champagne. use to fill wine glass',3.5,NULL)," +
//        "(127,'captain''s colada','',5.0,NULL)," +
//        "(128,'oreo martini','shake and double strain into coup',5.0,NULL)," +
//        "(129,'strawberry shortcake','well mix, shake and double strain',3.0,NULL)," +
//        "(130,'french martini','shake all and garnished with pineapple',0.0,NULL)," +
//        "(131,'romeo and juliet',replace('muddle cucumber and salt \nshake everything \ndouble strain\n\ngarnish with mint rose water and angustura bitters \n\n3drops rose water','\n',char(10)),0.0,NULL)," +
//        "(132,'cream egg','',0.0,NULL)," +
//        "(133,'basil orgeat lemon',replace('quick shake\ninto crushed ice with basil\ntop with soda','\n',char(10)),0.0,NULL)," +
//        "(134,'lemon delight','',4.0,NULL)," +
//        "(135,'mexican mia tai','shake all into tikki or coup',3.5,'bit sweet could try with Curacao instead')," +
//        "(136,'white lady','whip',0.0,'')," +
//        "(137,'twilight in queens','',0.0,NULL)," +
//        "(138,'raspberry blossom',replace('shake, possibly whip\ngrenadine in at end','\n',char(10)),0.0,'previously made with specific rasberry vinilla margarine gin. so sweet sour alcohol balance might be off')," +
//        "(139,'mr blacks martini','',4.0,NULL)," +
//        "(140,'rasberry rose',replace('blend all but lemonade \n\nup lemon juice/sugar depending on taste','\n',char(10)),5.0,NULL)," +
//        "(141,'tequila and mint','tbd',0.0,NULL)," +
//        "(142,'green demon','',0.0,NULL)," +
//        "(143,'purple rain','blue curacao for namesake',0.0,NULL);";
//
//        String defaultIngredientRecipeRelation = "INSERT INTO ingredientRelation VALUES(22,10,'4',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,10,'0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,10,'3',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,10,'4',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,11,'0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,11,'1',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,11,'2',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,11,'1',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,12,'0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,12,'3',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,12,'2',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,12,'3',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,13,'1',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,13,'1',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,13,'2',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,13,'1',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,20,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,20,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,20,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,20,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,21,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,21,'0.200000002980232',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,21,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,21,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,22,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,22,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,22,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,22,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,23,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,23,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,23,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,24,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,24,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,24,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,24,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,24,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,25,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,25,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,25,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,25,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,26,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,26,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,26,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,27,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,27,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,27,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,28,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,28,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,28,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,29,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,29,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,29,'8.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,29,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,31,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,31,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,31,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,31,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,34,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,34,'0.300000011920929',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,34,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,34,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,35,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,35,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,35,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,37,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,37,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,37,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,37,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(27,37,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,38,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,38,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(28,38,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,38,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(27,38,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,38,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,40,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,40,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(19,40,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,40,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,41,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,41,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,41,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,41,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,41,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,41,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(11,43,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,43,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,43,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,43,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,44,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,44,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,44,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,44,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,45,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,45,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,45,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,45,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(9,46,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,46,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,46,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,46,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,47,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,47,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,47,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,47,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,47,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,47,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,48,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,48,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,48,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,48,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,50,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(36,50,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,50,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,50,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,50,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,50,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(35,51,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(34,51,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,51,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,51,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(16,52,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,52,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,52,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(19,52,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,49,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,49,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,49,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,49,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,49,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(40,49,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,54,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(38,54,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,54,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(43,56,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,56,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,56,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,56,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,55,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,55,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,55,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,55,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,57,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(45,57,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(44,57,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,59,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,59,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(29,59,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,59,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(47,59,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,4,'0.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,4,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,60,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,60,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(37,60,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,61,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(58,61,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(37,61,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(59,62,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,62,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(37,62,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,63,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,63,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(37,63,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(58,64,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(37,64,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(60,64,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(62,65,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(37,65,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(63,65,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,65,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(64,66,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,66,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(37,66,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(65,67,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,67,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(37,67,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(66,68,'9.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(67,68,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,68,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(32,68,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(50,69,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,69,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,70,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(58,70,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(68,70,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,71,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,71,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,71,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(58,72,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(60,72,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,72,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,73,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,73,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(68,73,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(63,73,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(70,75,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,75,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,75,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,75,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(74,76,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,76,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(76,76,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(32,76,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(75,76,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,76,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(77,77,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,77,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(78,77,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(63,77,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,77,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,77,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(28,78,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,78,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,78,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(74,80,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,80,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,80,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,80,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,81,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,81,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,81,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,81,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(66,83,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(28,83,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(32,83,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,83,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,84,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(79,84,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,84,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,84,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,84,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,84,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(80,85,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,85,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,85,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,85,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,86,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,86,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(19,86,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,86,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(82,87,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,87,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,87,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(58,79,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,79,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(60,79,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,88,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(80,88,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(32,88,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,89,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,89,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(19,89,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,89,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(83,89,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,90,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,90,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,90,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(28,90,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(2,90,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,90,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,90,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(43,91,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,91,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,91,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(28,91,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,91,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,91,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,91,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(43,82,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,82,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(28,82,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,82,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,92,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,92,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,92,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,92,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,93,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,93,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,93,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,93,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,93,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,94,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,94,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,94,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,94,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,95,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(42,95,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,95,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(85,96,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(84,96,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(42,96,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,97,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(42,97,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,97,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(34,98,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(42,98,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,98,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,99,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,99,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,99,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,99,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,99,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(42,100,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,100,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(86,100,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,101,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(42,101,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,101,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(86,101,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,5,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,5,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,5,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,102,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,102,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,102,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,103,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,103,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,103,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,103,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(27,103,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,33,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,33,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,33,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,33,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,104,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,104,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,104,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(1,104,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,36,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,36,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,36,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,105,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,105,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,105,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,105,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,6,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,6,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,6,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,6,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,32,'5.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,32,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,32,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,106,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,106,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,106,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,106,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,106,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,107,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,107,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,107,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,108,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,108,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,108,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,108,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,108,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(27,108,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,30,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,30,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,30,'0.300000011920929',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,30,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,109,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,109,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,109,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,109,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,109,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,110,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(87,110,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,110,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,110,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,110,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,111,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,111,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(87,111,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,111,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,112,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(74,112,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(88,112,'8.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,112,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,112,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,112,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(87,112,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,112,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,112,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,53,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(90,53,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(69,53,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,53,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(42,53,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,53,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,39,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(31,39,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,39,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(28,39,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(91,39,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,39,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(29,39,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(32,39,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(85,113,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,113,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,113,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,113,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(92,114,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,114,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,114,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(92,115,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,115,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,115,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(85,116,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(42,116,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,116,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,116,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(85,117,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,117,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,117,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,117,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(92,118,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,118,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(93,118,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,119,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(69,119,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,119,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,119,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(63,119,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(19,119,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,119,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,119,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(94,120,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(80,120,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,120,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,120,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(33,121,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,121,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,121,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,121,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,121,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,122,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(95,122,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,122,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,122,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,122,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(95,123,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,123,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,123,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,123,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,123,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(95,125,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,125,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,125,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,125,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(96,125,'0.200000002980232',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,126,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(80,126,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,126,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(1,126,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(96,126,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,127,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(31,127,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,127,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,127,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(29,127,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,127,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(92,128,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(97,128,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,128,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,128,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,129,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(39,129,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,129,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,130,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,130,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(98,130,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(74,131,'0.100000001490116',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(100,131,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(66,131,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(99,131,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,131,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,131,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,131,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,131,'0.699999988079071',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(92,132,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(34,132,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(102,132,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(101,132,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(90,132,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(69,132,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(103,133,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,133,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(63,133,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(104,133,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(100,134,'0.200000002980232',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(13,134,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,134,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,134,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,134,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(105,135,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(9,135,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(41,135,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,135,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(104,135,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,136,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(19,136,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(93,136,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,136,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(20,42,'4.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,42,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(26,42,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(43,137,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,137,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,137,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(107,137,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(21,137,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,137,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(108,138,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(88,138,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,138,'0.200000002980232',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,138,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,138,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,138,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(106,139,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,139,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,139,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(86,139,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(100,140,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(88,140,'6.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,140,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,140,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(17,140,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(24,140,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(5,140,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(66,141,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(6,141,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(29,141,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(43,142,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,142,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,142,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,142,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,142,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,142,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(43,143,'0.5',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,143,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,143,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(8,143,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(23,143,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(22,124,'3.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(7,124,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(30,124,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(69,124,'2.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(4,124,'1.0',NULL);" +
//        "INSERT INTO ingredientRelation VALUES(25,124,'0.5',NULL);";
//
//
//        String[] args = new String[0];
//        db.rawQuery(defaultIngredients, args);
//        db.rawQuery(defaultRecipeList, args);
//        db.rawQuery(defaultIngredientRecipeRelation, args);
//    }

    private int countRecipes(SQLiteDatabase db) {
        String sql = "SELECT * FROM " + RecipeEntry.TABLE_NAME;
        String[] selctArgs = new String[0];
        try (Cursor cursor = db.rawQuery(sql, selctArgs)) {
            return cursor.getCount();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DELETE FROM " +  RecipeEntry.TABLE_NAME );
//        db.execSQL("DELETE FROM " + DrinkEntry.TABLE_NAME);
//        db.execSQL("DELETE FROM " + IngredientRelation.TABLE_NAME);
    }

}
