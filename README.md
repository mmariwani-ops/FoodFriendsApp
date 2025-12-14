FoodFriends – Projektdokumentation

Problembeskrivning
FoodFriends är en Android-applikation som löser problemet med opålitliga och opersonliga restaurangrecensioner. I stället för att visa anonyma betyg från okända användare gör FoodFriends det möjligt att se restaurangupplevelser som delas av ens egna vänner. Detta skapar ett mer trovärdigt och socialt sätt att upptäcka nya restauranger.

Hur applikationen löser problemet
Applikationen kombinerar Firebase Authentication och Cloud Firestore för att skapa ett säkert socialt nätverk där användare kan:

• Skapa inlägg (posts) om restauranger
• Lägga till vänner via e-post
• Skicka, acceptera och avvisa vänförfrågningar
• Se vänners restauranginlägg i ett gemensamt flöde
• Redigera och ta bort sina egna inlägg

Firestore Security Rules säkerställer att endast rätt användare kan läsa eller ändra information.

Hur applikationen fungerar:

Användare loggar in eller registrerar sig. Varje användare har ett användarobjekt i databasen under /users/{userId} i Firebase.

Vänsystemet fungerar genom två underkollektioner:
• friend_requests – inkommande vänförfrågningar
• friends – accepterade vänner

När en vänförfrågan accepteras skapas en relation åt båda hållen i friends.

Alla inlägg (posts) lagras i /posts. Varje post innehåller:
• userId
• restaurantName
• rating
• comment
• timestamp

Startsidan (Friends’ Activity) laddar användarens vänner och visar deras inlägg. Endast inlägg från accepterade vänner visas.

Hur man använder applikationen

Logga in eller registrera ett konto

Lägg till vänner via e-post

Acceptera inkommande vänförfrågningar

Skapa ett inlägg

Se vänners inlägg i Friends’ Activity

Redigera eller ta bort egna inlägg

Extra funktionalitet (Betygsgrundande del)

Följande extra funktioner har implementerats (utöver de som står i kraven):

• Vänbaserad åtkomst: användare kan endast se inlägg från sina accepterade vänner
• Säkert vänsystem: endast mottagaren kan läsa och hantera vänförfrågningar
• Redigering och borttagning av inlägg: endast ägaren kan ändra sina posts
• Realtidsflöde: vänners inlägg visas direkt via Firebase
• Tydlig MVVM-arkitektur med separerad UI- och datalogik

Arbetsfördelning i gruppen:

Hadi Moucho – Backend och Firebase
Ansvarade för:
• Firebase Authentication
• Cloud Firestore-struktur
• Firestore Security Rules
• Vänsystemets backendlogik
• CRUD-funktionalitet för posts
• Repositories (Friends, Users, Posts)

Fokus låg på backendlogik och korrekt behörighetsstyrning.

Mirko Mariwani – Frontend och UI
Ansvarade för:
• Jetpack Compose-UI
• Navigation och NavGraph
• Screens (Home, Friends, Posts, Edit Post, Profile)
• UI-state och användarinteraktion
• Visuellt flöde och användarupplevelse

Detta är och var arbetsfördelningen, men självklart har vi hjälpt varandra vardera med både frondend, ui samt backend.
Fokus låg på tydlig UX, navigation och visuell struktur.

Git: Mama mia git! Vi borde ha gått en kurs i git. Utöver ursäkterna för trasig git historik, stötte vi på svårigheter och strul konstant. Exempelvis, vi gjorde en push på git men det visade sig att något gick fel och hela projektet försvann så därför har vi varit väldigt rädda med att använda git. Flera försök gjordes ändå efter trots "rädslan" men då försvann inte projektet, utan repositoryn och alla commits etc. Git var väldigt stökigt. 

