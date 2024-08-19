# java4
BazyDanych

Na cwiczeniach pokazemy mozliwosci laczenia sie w Javie z np. MySQL-owa baza danych uzywajac interfejsu JDBC (Java Database Conectivity).


I. Odczyt z bazy danych:
========================


(a) Odpalic MySQL-owa baze danych: mozna skorzystac np. z SZBD WampServer (login:root, haslo:) lub innego SZBD, badz oczywiscie czystej formy mysql. 
    
(b) Utworzyc w MySQL baze danych "osoby", a niej tabele "studenci" postaci:

		
            |--------------------------------------|
            |  id  |   imie   |  nazwisko |  wiek  |
            |--------------------------------------|
            |   1  |  Pawel   | Miednica  |  41    |
            |--------------------------------------|
            |   2  |  Tomasz  | Gniewko   |  34    |
            |--------------------------------------|
            |   3  |  Edek    | Malizna   |  39    |
            |--------------------------------------|
            |   4  |  Gienek  | Los       |  65    |
            |--------------------------------------|
   
(c) Aby podlaczyc sie z ta baza w NetBeansie nalezy:

 - utworzyc nowy projekt

 - podpiac sterownik do bazy: mysql-connector-java-x.x.x-bin.jar (jest tez w katalogu NetBeansa:
   C:\Program Files\NetBeans x.x.x\idex\modules\ext\mysql....., jest na stronie MySQL).
   
   Najlepiej stojac na projekcie, prawy przycisk myszy i properties, a nastepnie zakladka Libraries i Add Jar/Folder
   
   Od wersji 6.1 NetBeansa sterownik do bazy MySQL juz jest w systemie i mozna go wybrac.

 - importowac pakiet: 
						java.sql.*;

 - zarejestrowac sterownik w systemie (tj. stworzyc obiekt Driver)

				Class.forName("com.mysql.jdbc.Driver"); 
								  
 - pozyskac obiekt polaczenia z baza danych:
						
				String url = "jdbc:mysql://localhost:3306/osoby";
				String login = "root";
				String password = "";
				Connection conn = DriverManager.getConnection(url,login,password);

(d) Wykonajmy teraz proste zapytanie do bazy:

				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM studenci");
   
   ResulSet jest wynikowym zbiorem zapytan skladajacym sie z wierszy (rekordow) i kolumn (pol).

   Mozemy przegladac caly ten zbior zakladajac np. petle while i przegladajac kolejne rekordy.
   
   Dostep do kolejnego rekordu (wiersza zbioru ResulSet) uzyskujemy metoda

				next()

   natomiast czytanie wartosci kolejnych pol danego rekordu mozna uzyskac metoda

							
				getString(String columnLabel)
							
   lub
   
				getString(int columnIndex)
        
  Uwaga!!! Powinnismy zamykac metoda close(); po wykorzystaniu obiekty typu: Connection, ResultSet i Statement.

   
Okazuje sie, ze mozna bez problemu odczytac pewne meta-dane odnosnie zbioru ResulSet, tj. dodatkowe informacje takie jak:
    
- nazwy kolumn 

- liczbe kolumn itp. 
  
Zrobilibysmy to wywolujac metode getMetaData(), tj.

				
				rs.getMetaData()
							
							
dostajac obiekt typu ResultSetMetaData.


II. Zapis do bazy danych:
=========================

Napisac program ktory do bazy "osoby" doda kolejna tabele "przedmioty" postaci:
  

            |--------------------------------|
            |    przedmiot    |   semestr    |
            |--------------------------------|
            |	 matematyka   |      2       |
            |--------------------------------|
            |     fizyka      |      1       |
            |--------------------------------|
            |     polski      |      1       |
            |--------------------------------|

  
   Stworzyc najpierw tabele "przedmioty" korzystac z metody executeUpdate wywolywanej na obiekcie st typu Statement
   z odpowiednim wpisem SQL-owym, tj. korzystajac z polecenia:
   
			
				CREATE TABLE przedmioty(przedmiot VARCHAR(20),semestr INT);


   Wypelnianiee kolejnych rekordow mozna teraz robic:
   

				st.executeUpdate("INSERT INTO przedmioty VALUES ('matematyka','2')");
		
   lub korzystajac z parametryzowanych zapytan (co jest wydajniejsze przy wielokronym wykorzystywaniu). Trzeba by zrobic to tak:
                                                
				PreparedStatement stpre = conn.prepareStatement("INSERT INTO przedmioty VALUES (?,?)");
				stpre.setString(1,"matematyka");
				stpre.setInt(2,Integer.parseInt("3"));
				stpre.executeUpdate();
                                              
Uwaga!!!


    Powinnismy miec teraz dwie dostepne tabele w bazie "osoby" a mianowicie: "studenci" i "przedmioty".

    Mozemy rowniez pobrac pewne meta-dane dotyczace wszystkich tabel w naszej bazie. Nalezaloby wywolac:

                                 
				DatabaseMetaData daneBazy = conn.getMetaData();
    
    
   Widzimy ze dane te teraz przechowywane w zmiennej danebazy typu DatabaseMetaData.

   Aby np. dostac nazwe wszystkich tabel w naszej bazie powinnismy wywolac:


				ResultSet nazwyTabel = daneBazy.getTables(null, null, null, new String[]{"TABLE"});
				while(nazwyTabel.next())
				{
					System.out.println("tabela: "+nazwyTabel.getString(3));
				}

Zadanie do domu
===============

Napisac program, ktory da mozliwosc z danej bazy danych przechowywanej
w SZBD np. mysql wyswietlenia i modyfikacji danych z wybranej tabeli.

Program ma dzialac w trybie konsolowym lub okienkowym.

A zatem, po zgloszeniu programu powinnismy zobaczyc wszystkie dostepne tabele w bazie.
Nastepnie powinnismy miec mozliwosc wybrania dowolnej tabeli, odczytania jej zawartosci
i zapisania jej ponownie do bazy.

Dodatkowo zapewnic mozliwosc dodawania i usuwania danych w tabeli. 