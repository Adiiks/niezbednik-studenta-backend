<h1> Niezbędnik Studenta - REST API </h1>

Jest to część backendowa aplikacji społecznościowej Niezbędnik Studenta, która jest przeznaczona dla studentów wydziału 
Matematyki i Informatyki Uniwersytetu im. Adama Mickiewicza. Aplikacja stanowi repozytorium wiedzy na temat przedmiotów
nauczanych na powyżej wspomnianym wydziale. Umożliwia studentom dzielenie się wiedzą oraz materiałami naukowymi. Aplikacja składa się z następujących modułów:

<h2> Autentykacja i autoryzacja </h2>

Aplikacja łączy się z systemem USOS w celu autentykacji użytkowników. Użytkownicy mogą posiadać jedną z dwóch lub obie role (zalogowany użytkownik, admin).

<h2> Forum </h2>

- dodawanie, usuwanie, edytowanie postów/komentarzy
- przeglądanie postów/komentarzy
- likowanie postów/komentarzy
- akceptowanie komentarza jako znalezionej odpowiedzi na zadane pytanie (podobnie jak na stackoverflow)

<h2> Prowadzący zajęcia </h2>

- przeglądanie listy osób, które prowadzą zajęcia na uczelni
- dodawnie, aktualizowanie, usuwanie prowadzących
- przeglądanie przedmiotów nauczanych przez danego prowadzącego

<h2> Przedmioty (zajecia) </h2>

- przeglądanie listy przedmiotów
- dodawanie, aktualizowanie, usuwanie przedmiotów
- dołączanie do przedmiotu (aby uzyskać dostęp do takcih rzeczy jak np. forum dotyczące tego przedmiotu)
- opuszczanie przedmiotu
- przegląd uczestników należących do danego przedmiotu
- przegląd wszystkich przedmiotów, do któych zapisał się użytkownik
- dodawanie/usuwanie plików do/z przedmiotów (np. notatki z zajęć)
- przegląd listy plików dodanych do danego przedmiotu

<h2> Ogłoszenia, Korepetycje </h2>

- dodawanie, usuwanie, aktualizowanie ogłoszeń/korepetycji
- przegląd ogłoszeń/korepetycji
- zgłaszanie cheć udziału w korepetycjach
 
<h2> Powiadomienia </h2>

Wysyłanie i odczytywanie powiadomień przy pomocy Firebase Cloud Messages

<h2> Zgłoszenia do adminów </h2>

- utworzenie, usuwanie zgłoszenia
- przegląd zgłoszeń

<h2> Niezbędnik Studenta - Frontend </h2>
Aby korzystać z pełni funkconalnej aplikacji Niezbędnik Studenta należy także zapoznać się z częścią frontendową
aplikacji, znajdującą się https://github.com/PhillJaySaw/niezbednik-studenta-front-open

<h2> Praca z Niezbędnik Studenta w twoim IDE </h2>
Na twoim komputerze musisz mieć zainstalowane: Java 11 oraz preferowane przez Ciebie IDE (IntelliJ IDEA, Eclipse, itp.)

<h2> Konfiguracja projektu </h2>
Aby aplikacja działała poprawnie trzeba skonfigurować dwie rzeczy:

<h4> Konfiguracja USOS </h4>
W celu autentykacji i autoryzacji użytkowników aplikacja łączy się z Usosem. Aby poprawnie skonfigurować połączenie trzeba wejść na stronę https://usosapps.amu.edu.pl/developers/ i wypełnić formularz
w celu otrzymania klucza publicznego i prywatnego. Po otrzymaniu tych kluczy, trzeba je wkleić w pliku application.properties w polach consumer.key=XXX oraz consumer.secret=XXX (w miejsca XXX).

<h4> Konfiguracja Firebase </h4>
Aplikacja Niezbędnik Studenta wysyła powiadomienia do użytkowników przez usługę Firebase Cloud Messaging. Trzeba zalogować się i utworzyć projekt firebase na stronie https://firebase.google.com.
Następnie przejść do ustawień projektu -> konta usługi i kliknać wygeneruj klucz prywatny. Po jego wygenerowaniu otrzymamy plik, którego zawartość trzeba przekopiować do pliku w naszym projekcie
firebase.json, który znajduje się w folderze resources.  

<h2> Uruchamianie aplikacji lokalnie </h2>
Aplikacja Niezbędnik Studenta jest zbudowana za pomocą Spring Boota i Mavena. Aby ją uruchomić należy pobrać
repozytorium kodu, przejść do niego i wpisać w podanej kolejności dwie komendy: maven package, a 
następnie mvn spring-boot:run. Wymagane jest mieć zainstalowane Mavena na komputerze, aby móc korzystać z tych komend.




















