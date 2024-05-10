import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();
        int kontrol = 0, tempx, tempy;
        int skor = 0, dusman_skor = 0;
        int[][] matris;
        boolean[][] atisYapildiMi; // Kullanıcının daha önce atış yaptığı koordinatları tutacak boolean matrisi

        // Zorluk seviyesini belirleme
        int zorlukSeviyesi = 0; // 0: Kolay, 1: Orta, 2: Zor
        System.out.println("Zorluk Seviyesini Seciniz: ");
        System.out.println("0 - Kolay");
        System.out.println("1 - Orta");
        System.out.println("2 - Zor");
        zorlukSeviyesi = sc.nextInt();

        // Atış hakkı belirleme
        int atisHakki = 0;
        int mayinSayisi = 0;
        if (zorlukSeviyesi == 0) {
            matris = new int[5][5];
            atisYapildiMi = new boolean[5][5];
            atisHakki = 30; // Kolay seviyede 15 atış hakkı
            mayinSayisi = 3; // Kolay seviyede 3 mayın
        } else if (zorlukSeviyesi == 1) {
            matris = new int[7][7];
            atisYapildiMi = new boolean[7][7];
            atisHakki = 10; // Orta seviyede 10 atış hakkı
            mayinSayisi = 6; // Orta seviyede 6 mayın
        } else {
            matris = new int[9][9];
            atisYapildiMi = new boolean[9][9];
            atisHakki = 5; // Zor seviyede 5 atış hakkı
            mayinSayisi = 9; // Zor seviyede 9 mayın
        }

        // Başlangıçta matrisin tüm elemanlarını 1 yaparak deniz alanını belirle
        for (int i = 0; i < matris.length; i++) {
            for (int j = 0; j < matris[i].length; j++) {
                matris[i][j] = 1;
            }
        }

        // Gemilerin yerlerini kullanıcıya yerleştirme
        int q = 1;
        while (q <= 3) {
            System.out.println(q + ". gemi X koordinati: ");
            int x = sc.nextInt();
            System.out.println(q + ". gemi Y koordinati: ");
            int y = sc.nextInt();

            // Kullanıcının daha önce atış yapmak için seçtiği koordinatları kontrol etme
            if (atisYapildiMi[x][y]) {
                System.out.println("Bu koordinata daha once atis yapilmisti. Lutfen baska bir koordinat girin.");
                continue;
            }

            // Kullanıcının girdiği koordinatların matris sınırları içinde ve daha önce atış yapılmamış olduğunu kontrol etme
            if (x >= 0 && x < matris.length && y >= 0 && y < matris[0].length && (matris[x][y] == 1 || matris[x][y] == 2)) {
                matris[x][y] = 2; // Gemiyi yerleştir
                q++;
            } else {
                System.out.println("Gecersiz koordinatlar veya daha once secilmis bir konum. Lutfen tekrar deneyin.");
            }
        }

        // Gemilerin yerlerini saklamak için liste oluşturma
        List<int[]> bilgisayarGemileri = new ArrayList<>();

        // Bilgisayarın gemilerini rastgele yerleştirme ve koordinatlarını saklama
        int gemiSayisi = 3; // Her seviyede 3 gemi yerleştirilecek
        for (int i = 0; i < gemiSayisi; i++) {
            int x, y;
            do {
                x = rnd.nextInt(matris.length);
                y = rnd.nextInt(matris[0].length);
            } while (matris[x][y] != 1);
            matris[x][y] = 3; // Bilgisayarın gemileri
            bilgisayarGemileri.add(new int[]{x, y}); // Gemilerin koordinatlarını sakla
        }
      // Amiral gemisini rastgele yerleştirme
              int amiralGemiBoyutu = 2;
              int amiralX, amiralY;
              do {
                  amiralX = rnd.nextInt(matris.length - amiralGemiBoyutu + 1);
                  amiralY = rnd.nextInt(matris[0].length - amiralGemiBoyutu + 1);
              } while (!isSpaceAvailableForAmiral(matris, amiralX, amiralY, amiralGemiBoyutu));

              for (int i = amiralX; i < amiralX + amiralGemiBoyutu; i++) {
                  for (int j = amiralY; j < amiralY + amiralGemiBoyutu; j++) {
                      matris[i][j] = 4; // Amiral gemisi
             }
      }

        // Mayınları rastgele yerleştirme
        for (int i = 0; i < mayinSayisi; i++) {
            int x, y;
            do {
                x = rnd.nextInt(matris.length);
                y = rnd.nextInt(matris[0].length);
            } while (matris[x][y] != 1);
            matris[x][y] = -1; // Mayınları işaretle
        }

        // Oyunun başında matrisi ekrana yazdırma
        printMatris(matris); // Bilgisayarın gemilerini ve mayınları saklayarak matrisi yazdır

      // Oyun döngüsü
      while (atisHakki > 0) {
          if (kontrol == 0) { // Sıra kullanıcıda
              System.out.println("Atis yapmak istediginiz X koordinati: ");
              tempx = sc.nextInt();
              System.out.println("Atis yapmak istediginiz Y koordinati: ");
              tempy = sc.nextInt();

              // Kullanıcının daha önce atış yapmak için seçtiği koordinatları kontrol etme
              if (atisYapildiMi[tempx][tempy]) {
                  System.out.println("Bu koordinata daha once atis yapilmisti. Lutfen baska bir koordinat girin.");
                  continue;
              }

              if (matris[tempx][tempy] == 3) {
                  System.out.println("Atis basarili! Vurulan dusman gemisi '0' ile degistiriliyor...");
                  matris[tempx][tempy] = 0;
                  skor += 10;
              } else if (matris[tempx][tempy] == 4) {
                  System.out.println("Tebrikler, amiral gemiyi vurdunuz! Oyunu kazandiniz!");
                  return;
              } else if (matris[tempx][tempy] == -1) {
                  System.out.println("Mayına denk geldiniz!");
                  atisHakki--; // Mayına denk geldiğinde atış hakkını azalt
                  kontrol = 2; // Sırayı karşı rakibe ver
                  continue;
              } else {
                  System.out.println("Hatali atis yapildi...");
              }

              // Atış yapılan koordinatı işaretleme
              atisYapildiMi[tempx][tempy] = true;

              // Atış sonrası matrisin güncel halini ekrana yazdır
              printMatris(matris); // Bilgisayarın gemilerini ve mayınları saklayarak matrisi yazdır

              kontrol = 1;
          } 
          else if (kontrol == 2) { // Sıra karşı rakipte (mayın denk gelindiğinde)
              // İki adet rastgele tahmin yapılacak
              for (int i = 0; i < 2; i++) {
                  tempx = rnd.nextInt(matris.length);
                  tempy = rnd.nextInt(matris[0].length);
                  if (matris[tempx][tempy] == 2) {
                      System.out.println("Dusman birlikleri geminizi vurdu...");
                      matris[tempx][tempy] = 0;
                      dusman_skor += 10;
                  } else {
                      System.out.println("Dusman gemisini iskaladi...");
                  }
              }
              kontrol = 0; // Sırayı kullanıcıya geri ver
          } 
          else if(kontrol ==3){
            // İki adet tahmin yapılacak
            for (int i = 0; i < 2; i++) {
              System.out.println("Atis yapmak istediginiz X koordinati: ");
              tempx = sc.nextInt();
              System.out.println("Atis yapmak istediginiz Y koordinati: ");
              tempy = sc.nextInt();

              // Kullanıcının daha önce atış yapmak için seçtiği koordinatları kontrol etme
              if (atisYapildiMi[tempx][tempy]) {
                  System.out.println("Bu koordinata daha once atis yapilmisti. Lutfen baska bir koordinat girin.");
                  continue;
              }

              if (matris[tempx][tempy] == 3) {
                  System.out.println("Atis basarili! Vurulan dusman gemisi '0' ile degistiriliyor...");
                  matris[tempx][tempy] = 0;
                  skor += 10;
              } else if (matris[tempx][tempy] == 4) {
                  System.out.println("Tebrikler, amiral gemiyi vurdunuz! Oyunu kazandiniz!");
                  return;
              } else if (matris[tempx][tempy] == -1) {
                  System.out.println("Mayına denk geldiniz!");
                  atisHakki--; // Mayına denk geldiğinde atış hakkını azalt
                  kontrol = 2; // Sırayı karşı rakibe ver
                  break;
              } else {
                  System.out.println("Hatali atis yapildi...");
              }

              // Atış yapılan koordinatı işaretleme
              atisYapildiMi[tempx][tempy] = true;

              // Atış sonrası matrisin güncel halini ekrana yazdır
              printMatris(matris); // Bilgisayarın gemilerini ve mayınları saklayarak matrisi yazdır
            }
            kontrol = 1; // Sırayı kullanıcıya geri ver
          }
          else { // Sıra bilgisayarda
              tempx = rnd.nextInt(matris.length);
              tempy = rnd.nextInt(matris[0].length);
              if (matris[tempx][tempy] == 2) {
                  System.out.println("Dusman birlikleri geminizi vurdu...");
                  matris[tempx][tempy] = 0;
                  dusman_skor += 10;
              } 
              else if (matris[tempx][tempy] == -1) {
                System.out.println("Mayına denk geldiniz!");
                atisHakki--; // Mayına denk geldiğinde atış hakkını azalt
                kontrol = 3; // Sırayı karşı rakibe ver
                break;
              }
              else {
                  System.out.println("Dusman gemisini iskaladi...");
              }
              kontrol = 0;
          }

          // Atış hakkını azaltma
          atisHakki--;

          // Oyun bitiş koşulları kontrolü
          if (skor == 30 || dusman_skor == 30 || atisHakki == 0) {
              System.out.println("Oyun bitti. Kazanan: ");
              if (skor == 30) {
                  System.out.println("Tebrikler, siz kazandiniz!");
                  System.out.println("Puaniniz: " + skor);
              } else if (dusman_skor == 30) {
                  System.out.println("Dusman birlikleri kazandi. Puaniniz: " + skor);
              } else {
                  System.out.println("Atis hakkiniz kalmadi. Puaniniz: " + skor);
              }
              break;
          }
      }

        System.out.println("Oyun sona erdi...");
    }
  // Amiral gemisinin yerleştirilebileceği alanın kontrolü
      public static boolean isSpaceAvailableForAmiral(int[][] matris, int x, int y, int boyut) {
          for (int i = x; i < x + boyut; i++) {
              for (int j = y; j < y + boyut; j++) {
                  if (matris[i][j] != 1) {
                      return false;
                  }
              }
          }
          return true;
  }

    // Matrisi ekrana yazdırmak için yardımcı fonksiyon
    public static void printMatris(int[][] matris) {
        for (int i = 0; i < matris.length; i++) {
            for (int j = 0; j < matris[i].length; j++) {
                if (matris[i][j] == 2) {
                    System.out.print(" [2] "); // Kullanıcı gemileri
                } else if (matris[i][j] == 3) {
                    System.out.print(" [ ] "); // Bilgisayarın gemisi koordinatları (gizli)
                } else if (matris[i][j] == 4) {
                    System.out.print(" [A] "); // Amiral gemisi
                } else if (matris[i][j] == -1) {
                    System.out.print(" [*] "); // Mayın
                } else {
                    System.out.print(" [" + matris[i][j] + "] ");
                }
            }
            System.out.println();
        }
    }
  
}
