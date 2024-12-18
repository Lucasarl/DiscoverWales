package com.example.discoverwales.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.discoverwales.LanguagePreferences;
import com.example.discoverwales.R;
import com.example.discoverwales.TranslatorHelper;
import com.example.discoverwales.TranslatorManager;

import android.net.Uri;
import android.widget.Toast;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformationFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment1 extends Fragment {
    private TranslatorHelper translatorHelper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "cardiff_castle";
    private MediaPlayer mediaPlayer;
    // TODO: Rename and change types of parameters
    private String museum;

    public InformationFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param museum Parameter 1.
     * @return A new instance of fragment InformationFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment1 newInstance(String museum) {
        InformationFragment1 fragment = new InformationFragment1();
        Bundle args = new Bundle();
        System.out.println("MUSEUM: "+museum);
        args.putString(ARG_PARAM1, museum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            museum = getActivity().getIntent().getStringExtra("museum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView= inflater.inflate(R.layout.fragment_information1, container, false);
        String selectedLanguage = LanguagePreferences.getLanguage(getContext());
        translatorHelper = TranslatorManager.getTranslator(selectedLanguage);
        ImageButton playButton = rootView.findViewById(R.id.play_button);
        ImageButton pauseButton = rootView.findViewById(R.id.pause_button);
        ImageButton restartButton = rootView.findViewById(R.id.restart_button);
        TextView audioTitle = rootView.findViewById(R.id.audio_title);

        int audioResId = getAudioResource(museum);
        mediaPlayer = MediaPlayer.create(getContext(), audioResId);

        audioTitle.setText(getAudioTitle(museum));
        translatorHelper.translateTextView(audioTitle);

        playButton.setOnClickListener(v -> {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Toast.makeText(getContext(),"Playing audio", Toast.LENGTH_SHORT).show();
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Toast.makeText(getContext(),"Audio paused", Toast.LENGTH_SHORT).show();
            }
        });

        restartButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                Toast.makeText(getContext(),"Audio restarted", Toast.LENGTH_SHORT).show();
            }
        });

        if (museum != null) {
            if (museum.equals("cardiff_castle")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("Cardiff Castle is one of Wales’ leading heritage attractions and a site of international significance. Located within beautiful parklands at the heart of the capital’s city centre, Cardiff Castle’s Romanesque walls and fairytale towers conceal 2,000 years of history.\n");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.cardiff_castle_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("The first Roman fort at Cardiff was probably established at the end of the 50s AD, on a strategic site that afforded easy access to the sea. The original intention was presumably to help subdue the local tribe’s people, who were known as the Silures. Archaeological excavations have indicated that a series of four forts, each a different size, occupied the present site at different times. The final fort was built in stone and impressive remains of these Roman walls can still be seen today. After the fall of the Roman Empire the fort may well have been abandoned, although the settlement outside remained and likely took its name from Caer-Taff, meaning fort on the Taff. After the Norman conquest, the Castle’s keep was built, re-using the site of the Roman fort. The first keep on the motte, erected by Robert Fitzhamon, Norman Lord of Gloucester, was probably built of wood. Further medieval fortifications and dwellings followed over the years.\n");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("The Castle passed through the hands of many noble families until in 1766, it passed by marriage to the Bute family. The 2nd Marquess of Bute was responsible for turning Cardiff into the world’s greatest coal exporting port. The Castle and Bute fortune passed to his son John, the 3rd Marquess of Bute, who by the 1860s was reputed to be the richest man in the world. In 1866 the 3rd Marquess began a collaboration with the genius architect, William Burges that would transform the Castle. Within gothic towers he created lavish and opulent interiors, rich with murals, stained glass, marble, gilding and elaborate wood carvings. Each room has its own special theme, including Mediterranean gardens and Italian and Arabian decoration. Despite both dying at relatively young ages, much of the work continued and many of their unfinished projects would be completed by the 4th Marquess.\n\n During the war years, from 1939 – 1945, Cardiff Castle played what will probably be the last defensive role in its long history. With the threat of aerial bombardment by the Nazi’s Luftwaffe hanging over the city, air raid shelters were created within tunnels in the Castle’s walls. When the sirens sounded, almost 2000 residents could take shelter here, protected by the layers of masonry and earth banks above.");

                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/y5r9loEntzc?si=UYV2qpFU3oObQ5VU\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("national_museum")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("National Museum Cardiff (Welsh: Amgueddfa Genedlaethol Caerdydd) is a museum and art gallery in Cardiff, Wales. The museum is part of the wider network of Amgueddfa Cymru – National Museum Wales (formerly the National Museums and Galleries of Wales). Entry is kept free by a grant from the Welsh Assembly Government.\n\nThe National Museum of Wales was founded in 1907, when it inherited the collection of the Cardiff Museum, which shared the building of Cardiff Central Library. Construction of a new building in the civic complex of Cathays Park began in 1912, but owing to the First World War it did not open to the public until 1927. The architects were Arnold Dunbar Smith and Cecil Brewer, although the building as it now stands is a heavily truncated version of their design.\n\n The museum has collections of archaeology, botany, fine and applied art, geology and zoology.");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.national_museum_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("The collection of Old Master paintings in Cardiff includes, among other notable works, The Virgin and Child between Saint Helena and St Francis by Amico Aspertini, The Poulterer's Shop by Frans Snyders and A Calm by Jan van de Cappelle. A collection of landscape paintings in the classical tradition includes works by Claude, Gaspard Dughet, Salvator Rosa and two works by Nicolas Poussin: The Funeral of Phocion and The Finding of Moses (the latter owned jointly by the Museum and the National Gallery, London). These works prefigure the career of the Welsh-born Richard Wilson, called \"the father of British landscape painting\". In 1979 four cartoons for tapestries illustrating scenes from the Aeneid were bought as works by Peter Paul Rubens, but the attribution is now disputed.\n");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("There is a gallery devoted to British patronage of the eighteenth century, in particular that of Sir Watkin Williams Wynn, who was nicknamed 'the Welsh Medici' for his lavish spending on the arts. Included is a portrait of Williams-Wynn in Rome with fellow Tourists by Pompeo Batoni, one of his second wife by Sir Joshua Reynolds and his chamber organ designed by Robert Adam. Other paintings of note from this period is a portrait of Viscountess Elizabeth Bulkeley of Beaumaris as the mythological character Hebe, by the 'sublime and terrible' George Romney, and Johann Zoffany's group portrait of Henry Knight, a Glamorgan landowner, with his children.\n");

                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-Mgs0uKsEkI?si=0db66jL3t2KLJ-Tt\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("cardiff_museum")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("The Museum of Cardiff explores the city’s story and heritage, telling the history of Cardiff through the eyes of those who created the city – its people.\n\n The Museum of Cardiff is a great starting point for any visit to Cardiff, and gives an introduction to the city’s history through engaging, interactive displays. It uses the stories of the people who have lived and worked in the city over past centuries to bring history to life.\n");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.cardiff_museum_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("This teapot has crossed the Atlantic Ocean twice.  It was given to Maggie Williams Harris in 1927 as she was about to board a ship at Cardiff Docks that would take her and her young family to Canada to start a new life.  When Maggie’s granddaughter emigrated back to Wales, she brought the teapot with her and donated it to the Cardiff Story.\n");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("Our collection is an important part of Cardiff’s heritage and helps us to tell the city’s story, from the earliest times, through the industrial coal boom, to the present day.\n\n Most of the objects, ephemera and photographs we collect have personal stories attached to them, so it is the people who have shaped the city that tell its story.\n\n We are always collecting! We continue to add to our collection to make sure we represent the diversity of Cardiff and its communities, past and present.\n\n We collect items from the past, and from today – contemporary collecting is important to us. Collecting objects and stories from the present means that future generations will know what Cardiff was like today.");

                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-Mgs0uKsEkI?si=nV8Ws5_7mNO6hDrh\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("st_fagans")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("St Fagans National Museum of History has been Wales’s most popular heritage attraction for many years. This museum holds a special place in the hearts of the people of Wales because it is a people’s museum, where you can explore history together through people’s everyday lives.\n\n It stands in the grounds of the magnificent St Fagans Castle, a late 16th-century manor house donated to the people of Wales by the Earl of Plymouth. During the last fifty years, over fifty original buildings from different locations in Wales and from different historical periods have been re-built in the 100-acre parkland. Each building is frozen in time and opens a door into Welsh history offering a fascinating glimpse into the past.\n");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.st_fagans_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("See how people in Wales have lived, worked and spent their leisure time. The re-erected buildings include farmhouses, a row of ironworkers’ cottages, a medieval church, a Victorian school, a chapel and a splendid Workmen’s Institute.\n The gardens at St Fagans are among the best in Wales. You will walk through elegant formal gardens of St Fagans castle or see the cottage gardens that provided food for working families. Native breeds of livestock can be seen in the fields and farmyards, and demonstrations of farming tasks take place daily.");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("There are also workshops where craftsmen still demonstrate their traditional skills. Their produce is usually on sale. Come and see one of the last working clogmakers in Wales or meet the blacksmith. There is a weaver working in an 18th century woolen mill and a miller producing flour in the 19th century corn mill.\n\n Visitors gain an insight into the rich heritage and culture of Wales, and the Welsh language can be heard in daily use amongst craftsmen and interpreters.\n\n");

                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Ra1dE7NllQg?si=bQSqmTU_FJddhGwq\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("coal_museum")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("Big Pit is a real coal mine and one of Britain's leading mining museums.\n\n With facilities to educate and entertain all ages, Big Pit is an exciting and informative day out. Enjoy a multi-media tour of a modern coal mine with a virtual miner in the Mining Galleries, exhibitions in the Pithead Baths and Historic colliery buildings.");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.coal_museum_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("All this AND the world-famous Underground Tour. Go 300 feet underground with a real miner and see what life was like for the thousands of men who worked at the coal face.\n\n An award-winning national museum that still retains many features of its former life as a coal mine, standing high on the heather-clad moors of Blaenafon, the tunnels and buildings that once echoed to the sound of the miners now enjoy the sound of the footsteps and chatter of visitors from all over the world.");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("The Museum is set in a unique industrial landscape, designated a World Heritage Site by UNESCO in 2000 in recognition of its international importance to the process of industrialisation through iron and coal production.\n");


                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/vCdIu68hViU?si=m1TMb0Tl7Axzmoux\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("legion_museum")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("Step back in time at the National Roman Legion Museum and explore life in a far-flung outpost of the mighty Roman Empire.\n\n Wales was one of the furthest outpost of the Roman Empire. In AD 75, the Romans built a fortress at Caerleon that would guard the region for over 200 years.\n");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.legion_museum_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("Learn what made the Romans a formidable force and how life wouldn't be the same without them. You'll be able to see Exhibitions and Artefacts that show us how they lived, fought, worshipped and died. Enjoy the sights, sounds and smells of our beautiful Roman Garden. At weekends and school holidays, children can step back in time in a full-sized barrack room, try on replica armour and experience the life of a Roman soldier. We pride ourselves on our award winning Educational facilities and Family Fun area.\n");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("Caerleon was one of only three permanent fortresses in Roman Britain. The museum lies inside what remains of the fortress. The Ruins include the most complete amphitheatre in Britain and the only remains of a Roman Legionary barracks on view anywhere in Europe. The National Roman Legion Museum researches, preserves and displays half a million objects from the Roman fortresses of Caerleon (Isca), Usk (Burrium) and their environs. It is an internationally important collection which provides evidence for life in two major Roman military bases, as well as life in civilian settlements that grew up around them.\n");


                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/TTheyBoO5a8?si=T6xMu29cBxLvG9dy\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("culture")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("Welsh culture is a vibrant tapestry woven from centuries of history, tradition, and creativity. At its heart lies a profound connection to the land, language, and community. The Welsh language, or Cymraeg, is a cornerstone of the nation’s identity, celebrated through music, poetry, and festivals like Eisteddfod, an annual event showcasing the best of Welsh arts.\n\n Music holds a special place in Welsh culture, earning Wales the title of \"The Land of Song.\" Traditional folk music, choral singing, and contemporary genres all thrive here. Instruments like the harp, especially the triple harp, are iconic symbols of Welsh musical heritage.");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.culture_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("Storytelling and literature are deeply ingrained in Welsh life. From the medieval tales of the Mabinogion to modern works by celebrated writers, the rich tradition of Welsh storytelling reflects themes of myth, nature, and resilience.\n\n Wales is also renowned for its ancient castles, which stand as testaments to its turbulent history and the spirit of its people. Festivals such as St. David’s Day celebrate the nation's patron saint and its enduring cultural pride.");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("In everyday life, community gatherings, rugby matches, and outdoor pursuits like hiking in the stunning landscapes of Snowdonia or along the Pembrokeshire Coast connect the people of Wales to their shared heritage and natural beauty. Welsh culture is not just preserved; it is lived, evolving with modernity while honoring its timeless roots.\n");


                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ufKf4eORcKA?si=Orjf9OJ55B8J1mBx\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("tradition")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("Wales is a country steeped in tradition. Even the Methodist revival in the 18th century, whose stern Puritanism banished the ancient Celtic traditions, was unable to stamp out all remains of their traditions.\n\n Today the old tales are kept alive by the Welsh speakers. There are an estimated 600,000 of them and the numbers are increasing. Traditional Welsh culture has been kept alive by the popularity of the Royal National Eisteddfod, a ceremonial gathering of musicians, poets and craftsmen.\n\n In the late 19th century children were not encouraged to speak Welsh in school. If they did so, they were punished by having a piece of wood called a ‘Welsh Not’ hung around their neck.");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.tradition_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("The Welsh Folk Museum at St. Fagans in Glamorgan has many folklore pieces. The carved wooden spoons, called ‘Love Spoons’, were carved by young men while they visited their sweethearts. The carving of these spoons apparently was encouraged by the young lady’s father as it ensured that the young man’s hands were kept occupied! The spoons are beautifully carved and combine both ancient Celtic designs and symbols of affection, commitment and faith.\n");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("Mining has long been a staple occupation in Wales and there are many superstitions and traditions associated with it.\n\n The Romans were the first to extensively mine for gold and lead. One of the largest lead mines was at Cwmystwth where in the 18th century silver was also mined. Dolaucothi near Pumpsaint is the site of a Roman gold mine, the only one in Britain. The gold near the surface was exploited by open-cast working and the deeper ore was reached underground by galleries. The galleries were drained by a timber water-wheel, part of which can be seen in the National Museum in Cardiff. Underground coal mining began in Wales over 400 years ago.\n\n In the past, superstitions were rife in all the coal mining communities and were always heeded!");


                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/AcvvWcDLagY?si=2v5N-yHcpRt8EwES\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("language")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("Offa’s Dyke was the first clear border between the English speakers of the east and the Welsh speakers of the west. Wales was one of the first countries to use its own language to create laws, and the word Cymry was used to describe its people as long ago as the seventh century.\n\n For the oldest existing set of Welsh tales from the medieval period, pay a visit to the National Library of Wales in Aberystwyth – coincidentally the home of the first private Welsh language school, in 1939 – for a look at 14th century tome the White Book of Rhydderch.\n");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.language_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("Norman invaders brought French to the valleys, and their British followers introduced English. In 1536, Henry VIII decided to pass the Act of Union, prohibiting the use of Welsh in public administration and the legal system. You can imagine Owain Glyndŵr, who had instigated a revolt at the start of the 15th century, turning in his grave against the ruling.\n\n Then there was the Act of Uniformity of 1549, which demanded all acts of public worship be conducted in English, and the somewhat contradictory legislation of Elizabeth I, who wanted churches to carry Welsh versions of the Book of Common Prayer and the Bible.\n\n The first book in Welsh was published around this time, and perhaps the peak of the wonderful poetry we know Wales for came in 1792, when Iolo Morganwg established the Gorsedd of the Bards, a protective alliance who played an important part in the rise of the Eisteddfod festivals.");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("The Industrial Revolution and World Wars perhaps accelerated the decline of the language, not helped by the infamous Welsh Not – signs hung around the necks of schoolchildren who dared speak Welsh during the 19th century. Now more than a quarter of the population of modern Cymru can speak or use Welsh. This figure increases among children, helped by two major education acts passed during the 1900s. You’ll hear and see plenty of Welsh – Radio Cymru launched in 1977, and the television channel, S4C, began in 1982. There are also plenty of newspapers, regional radio stations and road signs in both English and Welsh.\n");


                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/VQmDvb_YK3Y?si=XEGICbBahNGPIo1I\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("history")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("As a country, Wales began with Henry VIII's Act of Union in 1536. Before that time Wales had been a loose collection of independent kingdoms and lordships with influxes and incursions from Europe. It's believed that Wales, as an area of land, has been inhabited since 250,000 BC.\n\n The Welsh today are descended from many people. Celtic tribes from Europe came to settle the whole of the British isles around 500-100 BC, alongside the original Iron Age population.\n");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.history_2);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("It was their language which sowed the seeds of the modern Welsh language. Roman and Saxon invasions pushed the original Britons into the land area of Wales, where they became the Welsh people. Inward and outward migration has added diverse new layers of population across history.\n\n The origins of the Red Dragon flag, or \"y Draig Goch\", could date back to the Roman period, when the dragon (or draco) was used by Roman military cohorts at the time of the Emperor Trajan. After the Romans left, the Red Dragon remained as a key emblem of Wales and there are accounts of battles against the Saxons under the Red Dragon.\n");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("The Tudors adopted the Red Dragon, and the Welsh-born future Henry VII took to the battle of Bosworth Field under the Red Dragon standard.\n\n The origin of the word Wales is a strange one. It is a variation on a common word used hundreds of years ago by the Anglo Saxons to mean foreigners or outsiders. Variations of the same word can be found in other countries, such as Walloon part of Belgium.\n");


                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/dpnYoWH4nBg?si=F5hGrlAvEa0JfNH1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            } else if (museum.equals("news1")) {
                TextView textView = rootView.findViewById(R.id.first_text_view);
                textView.setText("For centuries, midway between the autumn equinox and winter solstice, the Welsh people have celebrated Calan Gaeaf on November 1. Nos Galan Gaeaf or “the evening before the first day of winter”, falls a day before, which the western world now recognises as Halloween. A time of year filled with monsters and ghouls, here are five spooky winter customs and beliefs unique to Wales and its people.\n");

                ImageView imageView = rootView.findViewById(R.id.image_view);
                imageView.setImageResource(R.drawable.news1);

                textView = rootView.findViewById(R.id.second_text_view);
                textView.setText("On Nos Galan Gaeaf, the horrifying hwch ddu gwta, or “tailess black sow”, would make its annual appearance. Usually a man draped in cloth or animal hide rising from dwindling fire embers, the hwch ddu would chase the village children home. As the fire died and the children anticipated the materialisation of the black sow, they would often chant a spooky verse, like: “Adref, adref am y cynta’, Hwch Ddu Gwta a gipio’r ola,” (“Home, home, at once, the tailess black sow shall snatch the last one.”)\n\n Juliette Wood, scholar of Celtic folklore, says the macabre ritual has its roots in beliefs about the souls of the dead, people and animals. But on a practical level, it was probably just an effective way of getting children to bed and teaching them about the dangers of straying from the group.\n");

                textView = rootView.findViewById(R.id.third_text_view);
                textView.setText("Fortune telling would have been rife at this time of year. Questions over who was next to be married, and who may meet an untimely death, were particularly popular. Women looking for love may have wandered around the bounds of a church, chanting “here is the sheath, where is the knife”, hoping to hear the name of the person they would marry as a response.\n\n In some parts of the country, stwmp naw rhyw, a mash made of nine different root vegetables with milk, butter, salt and pepper, would have a wedding ring placed at the centre. Whoever found the ring in their serving would be the next to be married.\n");


                WebView webView=rootView.findViewById(R.id.web_view);
                String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/E1smln0Yp1s?si=sRmu3KL3aVIvPbVq\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                webView.loadData(video,"text/html", "utf-8");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
            }
            if(museum.equals("cardiff_castle")||museum.equals("national_museum")||museum.equals("cardiff_museum")||museum.equals("st_fagans")||museum.equals("coal_museum")||museum.equals("legion_museum")) {
                LinearLayout buttonLayout = new LinearLayout(getContext());
                buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                buttonLayout.setWeightSum(3);

                ImageButton button1 = createImageButton(R.drawable.facebook_icon, getSocialMediaLink("facebook"));
                ImageButton button2 = createImageButton(R.drawable.twitter_icon, getSocialMediaLink("twitter"));
                ImageButton button3 = createImageButton(R.drawable.instagram_icon, getSocialMediaLink("instagram"));

                buttonLayout.addView(button1);
                buttonLayout.addView(button2);
                buttonLayout.addView(button3);

                LinearLayout rootLayout = rootView.findViewById(R.id.root_layout);
                rootLayout.addView(buttonLayout);
            }

            TextView textView1=rootView.findViewById(R.id.first_text_view);
            TextView textView2=rootView.findViewById(R.id.second_text_view);
            TextView textView3=rootView.findViewById(R.id.third_text_view);
            translatorHelper.translateTextView(textView1);
            translatorHelper.translateTextView(textView2);
            translatorHelper.translateTextView(textView3);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPreferences", MODE_PRIVATE);
            String textSize = sharedPreferences.getString("textSize", "Medium");

            float size = 18f; // Default medium size
            if ("Small".equals(textSize)) {
                size = 16f;
            } else if ("Large".equals(textSize)) {
                size = 24f;
            }

            textView1.setTextSize(size);
            textView2.setTextSize(size);
            textView3.setTextSize(size);

        }

        return rootView;

    }



    private String getSocialMediaLink(String platform) {
        switch (museum) {
            case "cardiff_castle":
                if (platform.equals("facebook")) {
                    return "https://www.facebook.com/officialcardiffcastle/?locale=en_GB";
                } else if (platform.equals("twitter")) {
                    return "https://x.com/cardiff_castle?lang=es";
                } else if (platform.equals("instagram")) {
                    return "https://www.instagram.com/cardiff_castle/";
                }
                break;
            case "national_museum":
                if (platform.equals("facebook")) {
                    return "https://www.facebook.com/museumcardiff/?locale=en_GB";
                } else if (platform.equals("instagram")) {
                    return "https://www.instagram.com/cardiffnationalmuseum/";
                } else if (platform.equals("twitter")) {
                    return "https://x.com/Museum_Cardiff?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";
                }
                break;
            case "cardiff_museum":
                if (platform.equals("facebook")) {
                    return "https://www.facebook.com/cardiffstory/?locale=en_GB";
                } else if (platform.equals("twitter")) {
                    return "https://x.com/TheCardiffStory?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";
                } else if (platform.equals("instagram")) {
                    return "https://www.instagram.com/museumofcardiff/";
                }
                break;
            case "st_fagans":
                if (platform.equals("facebook")) {
                    return "https://www.facebook.com/stfagansmuseum/?locale=en_GB";
                } else if (platform.equals("twitter")) {
                    return "https://x.com/StFagans_Museum?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";
                } else if (platform.equals("instagram")) {
                    return "https://www.instagram.com/stfagansmakersmarket/";
                }
                break;
            case "coal_museum":
                if (platform.equals("facebook")) {
                    return "https://www.facebook.com/bigpitmuseum/?locale=en_GB";
                } else if (platform.equals("twitter")) {
                    return "https://x.com/BigPitMuseum?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";
                } else if (platform.equals("instagram")) {
                    return "https://www.instagram.com/ncmme_miningmuseum/";
                }
                break;
            case "legion_museum":
                if (platform.equals("facebook")) {
                    return "https://www.facebook.com/romanlegionmuseum/?locale=en_GB";
                } else if (platform.equals("twitter")) {
                    return "https://x.com/RomanCaerleon?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";
                } else if (platform.equals("instagram")) {
                    return "https://www.instagram.com/explore/locations/254715255/national-roman-legion-museum/";
                }
                break;
            default:
                return "https://www.instagram.com/cardiff_castle/";
        }
        return "https://www.instagram.com/cardiff_castle/";
    }

    private ImageButton createImageButton(int imageResource, final String url) {
        ImageButton imageButton = new ImageButton(getContext());
        imageButton.setImageResource(imageResource);
        imageButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        imageButton.setAdjustViewBounds(true);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageButton.setBackground(null);
        imageButton.setOnClickListener(v -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        return imageButton;
    }

    private int getAudioResource(String museum) {
        switch (museum) {
            case "cardiff_castle":
                return R.raw.cardiff_castle_audio;
            case "national_museum":
                return R.raw.national_museum_audio;
            case "cardiff_museum":
                return R.raw.cardiff_museum_audio;
            case "st_fagans":
                return R.raw.st_fagans_audio;
            case "coal_museum":
                return R.raw.coal_museum_audio;
            case "legion_museum":
                return R.raw.legion_museum_audio;
            case "culture":
                return R.raw.culture_audio;
            case "tradition":
                return R.raw.tradition_audio;
            case "language":
                return R.raw.language_audio;
            case "history":
                return R.raw.history_audio;
            case "news1":
                return R.raw.history_audio;
            default:
                return R.raw.cardiff_castle_audio;
        }
    }

    private String getAudioTitle(String museum) {
        switch (museum) {
            case "cardiff_castle":
                return "Cardiff Castle Audio";
            case "national_museum":
                return "National Museum Audio";
            case "cardiff_museum":
                return "Cardiff Museum Audio";
            case "st_fagans":
                return "St Fagans Audio";
            case "coal_museum":
                return "Coal Museum Audio";
            case "legion_museum":
                return "Legion Museum Audio";
            case "culture":
                return "Culture Audio";
            case "tradition":
                return "Tradition Audio";
            case "language":
                return "Language Audio";
            case "history":
                return "History Audio";
            case "news1":
                return "News Audio";
            default:
                return "Museum Audio";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

}

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
}
    }