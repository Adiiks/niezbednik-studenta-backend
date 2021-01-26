<h1> Niezbędnik Studenta - REST API </h1>

Jest to część backendowa aplikacji społecznościowej Niezbędnik Studenta, która jest przeznaczona dla studentów wydziału 
Matematyki i Informatyki Uniwersytetu im. Adama Mickiewicza. Aplikacja stanowi repozytorium wiedzy na temat przedmiotów
nauczanych na powyżej wspomnianym wydziale. Umożliwia studentom dzielenie się wiedzą oraz materiałami naukowymi.

<h2> Niezbędnik Studenta - Frontend </h2>
Aby korzystać z pełni funkconalnej aplikacji Niezbędnik Studenta należy także zapoznać się z częścią frontendową
aplikacji, znajdującą się https://github.com/PhillJaySaw/NiezbednikStudentaFront

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




















