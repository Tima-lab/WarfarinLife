package com.example.warfarinlife.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 14;
    public static final String DATABASE_NAME = "WarfarinDB";
    public static final String TABLE_PROFILE = "Profiles";
    public static final String TABLE_LIST_BAN = "List_ban";
    public static final String TABLE_PROFILE_LIST_BAN = "Profile_list_ban";

    public static final String KEY_PROFILE_ID = "profile_id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_MNO = "MNO";
    public static final String KEY_DATE_ANALYSIS = "date_analysis";
    public static final String KEY_TIME_RECEPTION = "time_reception";

    //public static final String KEY_ID_LIST = "id_list";
    public static final String KEY_NAME_SUBSTANCE = "name_substance";
    public static final String KEY_INFO = "info";

    public static final String KEY_PROFILE_ID_LIST = "profile_id_list";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table " + TABLE_PROFILE + "(" + KEY_PROFILE_ID + " integer primary key AUTOINCREMENT," + KEY_NAME + " text not null, " +
                    KEY_MNO + " real, " + KEY_DATE_ANALYSIS + " numeric," + KEY_TIME_RECEPTION + " numeric" + ");");
        } catch(Exception ignored){}


        /*
        db.execSQL("create table " + TABLE_LIST_BAN + "(" + KEY_ID_LIST + " integer primary key AUTOINCREMENT," +
                KEY_NAME_SUBSTANCE + " text not null," + KEY_INFO + " text" + ");");
        */


        db.execSQL("create virtual table " + TABLE_LIST_BAN + " using fts4 ("  + KEY_NAME_SUBSTANCE + "," + KEY_INFO + ",tokenize=simple);");


        try {
            /*
            db.execSQL("create table " + TABLE_PROFILE_LIST_BAN + "(" + KEY_PROFILE_ID_LIST + " integer not null," + KEY_NAME_SUBSTANCE + " text not null, " +
                    KEY_INFO + " text" + ");");
             */
            db.execSQL("create virtual table " + TABLE_PROFILE_LIST_BAN + " using fts4 (" + KEY_PROFILE_ID_LIST + "," + KEY_NAME_SUBSTANCE + "," + KEY_INFO +",tokenize=simple);");
        } catch (Exception ignored){}


        ListReducing(db);
        ListIncrease(db);
        ListIncreaseOrReducing(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists " + TABLE_PROFILE);
        db.execSQL("drop table if exists " + TABLE_LIST_BAN);
        //db.execSQL("drop table if exists " + TABLE_PROFILE_LIST_BAN);

        onCreate(db);
    }

    private void InsertInList(SQLiteDatabase db, String name, String info) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME_SUBSTANCE, name);
        contentValues.put(KEY_INFO, info);
        db.insert(TABLE_LIST_BAN, null, contentValues);
    }

    public void InsertInProfileList(SQLiteDatabase db, int idProfile, String name, String info) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PROFILE_ID_LIST, idProfile);
        contentValues.put(KEY_NAME_SUBSTANCE, name);
        contentValues.put(KEY_INFO, info);
        db.insert(TABLE_PROFILE_LIST_BAN, null, contentValues);
    }

    public void ListReducing(SQLiteDatabase db) {
        InsertInList(db, "Колестирамин", "Снижение всасывания варфарина и влияние на энтерогепатическую циркуляцию");
        InsertInList(db, "Бозентан", "Индукция преобразования варфарина в CYP2C9/CYP3A4 в печени");
        InsertInList(db, "Апрепитант", "Индукция преобразования варфарина в CYP2C9");
        InsertInList(db, "Месалазин", "Возможность снижения антикоагулянтного эффекта варфарина");
        InsertInList(db, "Сукральфат", "Вероятность уменьшения абсорбции варфарина");
        InsertInList(db, "Гризеофульвин", "Снижение антикоагулянтного эффекта кумаринов");
        InsertInList(db, "Ретиноиды", "Возможность снижения активности варфарина");
        InsertInList(db, "Диклоксациллин", "Усиление метаболизма варфарина");
        InsertInList(db, "Рифампицин", "Усиление метаболизма варфарина. Необходимо избегать совместного применения данных препаратов");
        InsertInList(db, "Противовирусное средство (невирапин)", "Усиление метаболизма варфарина, опосредованного CYP2C9");
        InsertInList(db, "Противовирусное средство (ритонавир)", "Усиление метаболизма варфарина, опосредованного CYP2C9");
        InsertInList(db, "Нафциллин", "Снижение антикоагулянтного эффекта варфарина");
        InsertInList(db, "Феназон", "Индукция метаболизма энзимов, снижение концентрации варфарина в плазме. Может потребоваться увеличение дозировки варфарина");
        InsertInList(db, "Рофекоксиб", "Механизм взаимодействия неизвестен");
        InsertInList(db, "Барбитураты (например фенобарбитал)", "Усиление метаболизма варфарина");
        InsertInList(db, "Противоэпилептическое средство (карбамазепин)", "Усиление метаболизма варфарина");
        InsertInList(db, "Противоэпилептическое средство (вальпроевая кислота)", "Усиление метаболизма варфарина");
        InsertInList(db, "Противоэпилептическое средство (примидон)", "Усиление метаболизма варфарина");
        InsertInList(db, "Антидепрессант (тразодон)", "В четырех случаях клинического использования было установлено, что взаимодействие тразодона и варфарина вызывало снижение ПВ и МНО, но механизм данного взаимодействия неизвестен. Механизм взаимодействия варфарина и миансерина также неизвестен");
        InsertInList(db, "Антидепрессант (миансерин)", "В четырех случаях клинического использования было установлено, что взаимодействие тразодона и варфарина вызывало снижение ПВ и МНО, но механизм данного взаимодействия неизвестен. Механизм взаимодействия варфарина и миансерина также неизвестен");
        InsertInList(db, "Глутетимид", "Снижение антикоагулянтного эффекта варфарина вследствие усиления его метаболизма");
        InsertInList(db, "Хлордиазепоксид", "Снижение антикоагулянтного эффекта варфарина");
        InsertInList(db, "Аминоглутетимид", "Усиление метаболизма варфарина");
        InsertInList(db, "Азатиоприн", "Снижение всасывания варфарина и повышение метаболизма варфарина");
        InsertInList(db, "Меркаптопурин", "Снижение антикоагулянтного эффекта варфарина");
        InsertInList(db, "Митотан", "Возможно снижение антикоагулянтного эффекта варфарина");
        InsertInList(db, "Циклоспорин", "Варфарин повышает уровень циклоспорина или усиливает его эффект, оказывая влияние на метаболизм циклоспорина");
        InsertInList(db, "Колестирамин", "Может снижать антикоагулянтный эффект варфарина вследствие уменьшения его абсорбции");
        InsertInList(db, "Спиронолактон", "Прием диуретиков в случае выраженного гиповолемического действия может привести к увеличению концентрации факторов свертываемости, что уменьшает действие антикоагулянтов");
        InsertInList(db, "Хлорталидон", "Прием диуретиков в случае выраженного гиповолемического действия может привести к увеличению концентрации факторов свертываемости, что уменьшает действие антикоагулянтов");
        InsertInList(db, "Зверобой продырявленный (Hypericum perforatum)", "Усиливает метаболизм варфарина, осуществляемый CYP3A4 и CYP1A2 (метаболизм R-варфарина), а также осуществляемый CYP2C9 (метаболизм S-варфарина). Влияние индукции ферментов может сохраняться в течение 2 нед после окончания применения зверобоя продырявленного. В том случае, если пациент принимает препараты зверобоя продырявленного, следует измерить МНО и прекратить прием. Мониторирование МНО должно быть тщательным, т.к. его уровень может повыситься при отмене зверобоя продырявленного. После этого можно назначать варфарин");
        InsertInList(db, "Женьшень (Panax ginseng)", "Вероятна индукция преобразования варфарина в печени. Необходимо избегать совместного применения данных препаратов");
        InsertInList(db, "Витамин С", "Снижение антикоагулянтного эффекта варфарина");
        InsertInList(db, "Витамин К", "Варфарин блокирует синтез витамин-К-зависимых факторов свертывания");
    }

    public void ListIncrease(SQLiteDatabase db) {
        InsertInList(db, "Абциксимаб", "Дополнительное воздействие на систему свертывания крови");
        InsertInList(db, "Тирофибан", "Дополнительное воздействие на систему свертывания крови");
        InsertInList(db, "Эптифибатид", "Дополнительное воздействие на систему свертывания крови");
        InsertInList(db, "Клопидогрел", "Дополнительное воздействие на систему свертывания крови");
        InsertInList(db, "Гепарин", "Дополнительное воздействие на систему свертывания крови");
        InsertInList(db, "Циметидин", "Выраженное ингибирующее действие на систему цитохрома P450 (циметидин можно заменить ранитидином или фамотидином), приводящее к снижению метаболизма варфарина");
        InsertInList(db, "Глибенкламид", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Омепразол", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Амиодарон", "Снижение метаболизма варфарина после одной недели совместного приема. Данный эффект может сохраняться в течение 1–3 мес после отмены амиодарона");
        InsertInList(db, "Этакриновая кислота", "Может усиливать эффект варфарина вследствие вытеснения варфарина из белковых связей");
        InsertInList(db, "Гиполипидемическое средство (флувастатин)", "Конкуренция за метаболизм, опосредованный CYP2C9 и CYP3A4");
        InsertInList(db, "Гиполипидемическое средство (симвастатин)", "Конкуренция за метаболизм, опосредованный CYP2C9 и CYP3A4");
        InsertInList(db, "Гиполипидемическое средство (розувастатин)", "Конкуренция за метаболизм, опосредованный CYP2C9 и CYP3A4");
        InsertInList(db, "Гиполипидемическое средство (гемфиброзил)", "Конкуренция за метаболизм, опосредованный CYP2C9 и CYP3A4");
        InsertInList(db, "Гиполипидемическое средство (безафибрат)", "Конкуренция за метаболизм, опосредованный CYP2C9 и CYP3A4");
        InsertInList(db, "Гиполипидемическое средство (клофибрат)", "Конкуренция за метаболизм, опосредованный CYP2C9 и CYP3A4");
        InsertInList(db, "Гиполипидемическое средство (ловастатин)", "Конкуренция за метаболизм, опосредованный CYP2C9 и CYP3A4");
        InsertInList(db, "Гиполипидемическое средство (фенофибрат)", "Конкуренция за метаболизм, опосредованный CYP2C9 и CYP3A4");
        InsertInList(db, "Пропафенон", "Снижение метаболизма варфарина");
        InsertInList(db, "Хинидин", "Снижение синтеза факторов свертывания крови");
        InsertInList(db, "Диазоксид", "Может замещать варфарин, билирубин или другую высокосвязанную с белком субстанцию из белковых связей");
        InsertInList(db, "Дигоксин", "Усиление антикоагулянтного эффекта");
        InsertInList(db, "Пропранолол", "Усиление антикоагулянтного эффекта");
        InsertInList(db, "Тиклопидин", "Увеличение риска кровотечения. Необходимо мониторировать уровень МНО");
        InsertInList(db, "Дипиридамол", "Повышение уровня варфарина или дипиридамола вследствие потенцирования эффектов. Повышение риска кровотечений (геморрагий)");
        InsertInList(db, "Миконазол (в т.ч. в форме геля для полости рта)", "Снижение собственного клиренса варфарина и повышение свободной фракции варфарина в плазме; снижение метаболизма варфарина, опосредованного цитохромами Р450");
        InsertInList(db, "Стероидные гормоны — анаболические и/или андрогенные (даназол, тестостерон)", "Снижение метаболизма варфарина и/или прямое действие на системы коагуляции и фибринолиза");
        InsertInList(db, "Средства, действующие на щитовидную железу", "Усиление метаболизма витамин-К-зависимых факторов свертывания");
        InsertInList(db, "Глюкагон", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Аллопуринол", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Сульфинпиразон", "Усиление антикоагулянтного эффекта вследствие снижения его метаболизма и ослабления связей с белками");
        InsertInList(db, "Пенициллины в больших дозах (клоксациллин, амоксициллин)", "Возможность повышения вероятности кровотечения, включая кровотечения из десен, носа, появление нетипичных кровоподтеков или темный стул");
        InsertInList(db, "Тетрациклины", "Возможность усиления антикоагулянтного эффекта варфарина");
        InsertInList(db, "Сульфаниламид (сульфаметизол)", "Возможность усиления антикоагулянтного эффекта варфарина");
        InsertInList(db, "Сульфаниламид (сульфафуразол)", "Возможность усиления антикоагулянтного эффекта варфарина");
        InsertInList(db, "Сульфаниламид (сульфафеназол)", "Возможность усиления антикоагулянтного эффекта варфарина");
        InsertInList(db, "Хинолон (ципрофлоксацин)", "Снижение метаболизма варфарина");
        InsertInList(db, "Хинолон (норфлоксацин)", "Снижение метаболизма варфарина");
        InsertInList(db, "Хинолон (офлоксацин)", "Снижение метаболизма варфарина");
        InsertInList(db, "Хинолон (грепафлоксацин)", "Снижение метаболизма варфарина");
        InsertInList(db, "Хинолон (налидиксовая кислота)", "Снижение метаболизма варфарина");
        InsertInList(db, "Макролид (азитромицин)", "Снижение метаболизма варфарина");
        InsertInList(db, "Макролид (кларитромицин)", "Снижение метаболизма варфарина");
        InsertInList(db, "Макролид (эритромицин)", "Снижение метаболизма варфарина");
        InsertInList(db, "Макролид (рокситромицин)", "Снижение метаболизма варфарина");
        InsertInList(db, "Противогрибковое средство (флуконазол)", "Снижение метаболизма варфарина");
        InsertInList(db, "Противогрибковое средство (итраконазол)", "Снижение метаболизма варфарина");
        InsertInList(db, "Противогрибковое средство (кетоконазол)", "Снижение метаболизма варфарина");
        InsertInList(db, "Противогрибковое средство (метронидазол)", "Снижение метаболизма варфарина");
        InsertInList(db, "Хлорамфеникол", "Снижение метаболизма варфарина, выраженное ингибирующее действие на систему цитохрома P450");
        InsertInList(db, "Цефалоспорин (цефамандол)", "Усиление эффекта варфарина вследствие подавления синтеза витамин-К-зависимых факторов свертывания крови и других механизмов");
        InsertInList(db, "Цефалоспорин (цефалексин)", "Усиление эффекта варфарина вследствие подавления синтеза витамин-К-зависимых факторов свертывания крови и других механизмов");
        InsertInList(db, "Цефалоспорин (цефменоксим)", "Усиление эффекта варфарина вследствие подавления синтеза витамин-К-зависимых факторов свертывания крови и других механизмов");
        InsertInList(db, "Цефалоспорин (цефметазол)", "Усиление эффекта варфарина вследствие подавления синтеза витамин-К-зависимых факторов свертывания крови и других механизмов");
        InsertInList(db, "Цефалоспорин (цефоперазон)", "Усиление эффекта варфарина вследствие подавления синтеза витамин-К-зависимых факторов свертывания крови и других механизмов");
        InsertInList(db, "Цефалоспорин (цефуроксим)", "Усиление эффекта варфарина вследствие подавления синтеза витамин-К-зависимых факторов свертывания крови и других механизмов");
        InsertInList(db, "Сульфаметоксазол + триметоприм", "Снижение метаболизма варфарина и вытеснение варфарина из сайтов связывания с белками");
        InsertInList(db, "Левамизол", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Кодеин", "Комбинация кодеина и парацетамола усиливает активность варфарина");
        InsertInList(db, "Ацетилсалициловая кислота", "Вытеснение варфарина из альбуминов плазмы, ограничение метаболизма варфарина");
        InsertInList(db, "НПВС — включая азапропазон", "Конкуренция за метаболизм, осуществляемый ферментами цитохрома P450 CYP2С9");
        InsertInList(db, "Индометацин", "Конкуренция за метаболизм, осуществляемый ферментами цитохрома P450 CYP2С9");
        InsertInList(db, "Оксифенбутазон", "Конкуренция за метаболизм, осуществляемый ферментами цитохрома P450 CYP2С9");
        InsertInList(db, "Пироксикам", "Конкуренция за метаболизм, осуществляемый ферментами цитохрома P450 CYP2С9");
        InsertInList(db, "Сулиндак", "Конкуренция за метаболизм, осуществляемый ферментами цитохрома P450 CYP2С9");
        InsertInList(db, "Фепразон", "Конкуренция за метаболизм, осуществляемый ферментами цитохрома P450 CYP2С9");
        InsertInList(db, "Целекоксиб (за исключением ингибиторов ЦОГ-2)", "Конкуренция за метаболизм, осуществляемый ферментами цитохрома P450 CYP2С9");
        InsertInList(db, "Лефлуномид", "Ограничение метаболизма варфарина, опосредованного CYP2C9");
        InsertInList(db, "Парацетамол (ацетаминофен) (особенно после 1–2 нед постоянного приема)", "Ограничение метаболизма варфарина или влияние на образование факторов свертывания (данный эффект не проявляется при приеме менее 2 г парацетамола в день)");
        InsertInList(db, "Фенилбутазон", "Снижение метаболизма варфарина, вытеснение варфарина из сайтов связывания с белками. Данную комбинацию следует избегать");
        InsertInList(db, "Наркотические анальгетики (декстропропоксифен)", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Противоэпилептическое средство (фосфенитоин)", "Вытеснение варфарина из сайтов связывания с белками, повышение метаболизма варфарина");
        InsertInList(db, "Противоэпилептическое средство (фенитоин)", "Вытеснение варфарина из сайтов связывания с белками, повышение метаболизма варфарина");
        InsertInList(db, "Трамадол", "Конкуренция за метаболизм, опосредованный цитохромом Р450 3A4");
        InsertInList(db, "Антидепрессант: СИОЗС (флуоксетин)", "Ограничение метаболизма варфарина. Полагают, что СИОЗС ограничивают изофермент цитохрома Р450 CYP2C9. Он является ферментом, который метаболизирует наиболее сильнодействующий изомер S-варфарин. Кроме того, и СИОЗС, и варфарин прочно связываются с альбумином. При наличии обоих увеличивается возможность вытеснения одного из соединений из альбумина");
        InsertInList(db, "Антидепрессант: СИОЗС (флувоксамин)", "Ограничение метаболизма варфарина. Полагают, что СИОЗС ограничивают изофермент цитохрома Р450 CYP2C9. Он является ферментом, который метаболизирует наиболее сильнодействующий изомер S-варфарин. Кроме того, и СИОЗС, и варфарин прочно связываются с альбумином. При наличии обоих увеличивается возможность вытеснения одного из соединений из альбумина");
        InsertInList(db, "Антидепрессант: СИОЗС (пароксетин)", "Ограничение метаболизма варфарина. Полагают, что СИОЗС ограничивают изофермент цитохрома Р450 CYP2C9. Он является ферментом, который метаболизирует наиболее сильнодействующий изомер S-варфарин. Кроме того, и СИОЗС, и варфарин прочно связываются с альбумином. При наличии обоих увеличивается возможность вытеснения одного из соединений из альбумина");
        InsertInList(db, "Антидепрессант: СИОЗС (сертралин)", "Ограничение метаболизма варфарина. Полагают, что СИОЗС ограничивают изофермент цитохрома Р450 CYP2C9. Он является ферментом, который метаболизирует наиболее сильнодействующий изомер S-варфарин. Кроме того, и СИОЗС, и варфарин прочно связываются с альбумином. При наличии обоих увеличивается возможность вытеснения одного из соединений из альбумина");
        InsertInList(db, "Хлоралгидрат", "Механизм взаимодействия неизвестен");
        InsertInList(db, "Фторурацил", "Снижение синтеза ферментов цитохрома Р450 CYP2C9, метаболизирующих варфарин");
        InsertInList(db, "Капецитабин", "Снижение изоферментов CYP2C9");
        InsertInList(db, "Иматиниб", "Конкурентное подавление изофермента CYP3A4 и подавление метаболизма варфарина, опосредованного CYP2C9 и CYP2D6");
        InsertInList(db, "Ифосфамид", "Подавление CYP3A4");
        InsertInList(db, "Тамоксифен", "Тамоксифен, ингибитор CYP2C9, может повышать концентрацию варфарина в сыворотке вследствие снижения его метаболизма");
        InsertInList(db, "Метотрексат", "Усиление эффекта варфарина вследствие снижения синтеза прокоагулянтных факторов в печени");
        InsertInList(db, "Тегафур", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Трастузумаб", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Флутамид", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Циклофосфамид", "Вероятность изменения антикоагулянтного эффекта варфарина, т.к. циклофосфамид является противоопухолевым средством");
        InsertInList(db, "Этопозид", "Возможно усиление антикоагулянтного эффекта кумаринов");
        InsertInList(db, "Альфа- и бета-интерферон", "Увеличение антикоагулянтного эффекта и повышение концентрации варфарина в сыворотке вызывает необходимость снижения дозировки варфарина");
        InsertInList(db, "Дисульфирам", "Снижение метаболизма варфарина");
        InsertInList(db, "Метолазон", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Тиениловая кислота", "Усиление антикоагулянтного эффекта варфарина");
        InsertInList(db, "Зафирлукаст", "Повышение уровня или усиление эффекта зафирлукаста на фоне приема варфарина вследствие изменения метаболизма зафирлукаста");
        InsertInList(db, "Троглитазон", "Снижение уровня или ослабление эффекта варфарина вследствие изменения метаболизма варфарина");
        InsertInList(db, "Противогриппозная вакцина", "Возможность усиления антикоагулянтного эффекта варфарина");
        InsertInList(db, "Прогуанил", "Возможность усиления антикоагулянтного эффекта варфарина по данным отдельных сообщений");
        InsertInList(db, "Клюква", "Клюква снижает метаболизм варфарина, опосредованный CYP2C9");
        InsertInList(db, "Тонизирующие напитки, содержащие хинин", "Употребление большого количества тонизирующих напитков, содержащих хинин, может повлечь за собой необходимость снижения дозировки варфарина. Данное взаимодействие может быть объяснено снижением синтеза в печени прокоагулянтных факторов хинином");
        InsertInList(db, "Гинкго (Ginkgo biloba)", "Потенцирование антикоагулянтного/антитромбоцитарного эффекта может увеличить риск кровотечений");
        InsertInList(db, "Чеснок (Allium sativum)", "Потенцирование антикоагулянтного/антитромбоцитарного эффекта может увеличить риск кровотечений");
        InsertInList(db, "Дягиль лекарственный (Angelica sinensis)", "Потенцирование антикоагулянтного/антитромбоцитарного эффекта может увеличить риск кровотечений");
        InsertInList(db, "Папайя (Carica papaya)", "Потенцирование антикоагулянтного/антитромбоцитарного эффекта может увеличить риск кровотечений");
        InsertInList(db, "Шалфей (Salvia miltiorrhiza)", "Потенцирование антикоагулянтного/антитромбоцитарного эффекта может увеличить риск кровотечений");
    }

    public void ListIncreaseOrReducing(SQLiteDatabase db) {
        InsertInList(db, "Дизопирамид", "Может ослабить или усилить антикоагулянтный эффект варфарина");
        InsertInList(db, "Коэнзим Q10", "Коэнзим Q10 может усиливать или подавлять эффект варфарина из-за сходства химической структуры коэнзима Q10 и витамина К");
        InsertInList(db, "Алкоголь (этанол)", "Ингибирование или индукция метаболизма варфарина");

    }
}
