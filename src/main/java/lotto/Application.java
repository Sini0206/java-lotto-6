package lotto;

import camp.nextstep.edu.missionutils.Randoms;
import camp.nextstep.edu.missionutils.Console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Application {

    static final int PRICE = 1000;
    static int purchasePrice;
    static int ticketAmount;
    static ArrayList<List<Integer>> lottos = new ArrayList<>();
    static String [] inputNumbers;
    static int BonusNumber;
    static List<Integer> winningNumbers = new ArrayList<>();

    static boolean overlapExist = false;
    static boolean isRightRange = true;

    static int matchRecord = 0;
    static int [] lottoRecord = new int[lottos.size()];

    static enum Grade { first, second, third, fourth, fifth;}

    public static int getMoney(){
        System.out.println("구입금액을 입력해 주세요.");
        purchasePrice = Integer.parseInt(Console.readLine());
        return purchasePrice;
    }

    public static void getLotteryTickets(int money, int PRICE){
        int amount = money / PRICE;
        ticketAmount = amount;
    }

    public static void checkValidatePrice(int money,int PRICE){
        try {
            if (money % PRICE != 0)
                throw new IllegalArgumentException();
        }catch (IllegalArgumentException e){
            System.out.println("[ERROR] 구매금액이 나누어 떨어지지 않습니다.");
        }
    }

    static void createLotto(){
        for (int i = 0; i < ticketAmount; i++) {
            //  1.로또 번호 랜덤 뽑기
            List<Integer> lottoNumbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
            // 2. 로또 번호 오름차순 정렬
            Collections.sort(lottoNumbers);
            // 3. 로또 집합에 추가
            lottos.add(lottoNumbers);
            // 4. 로또 생성
            Lotto lotto = new Lotto(lottoNumbers);
        }
    }
    public static void printLottoNumbers(){
        System.out.println("\n" + ticketAmount + "개를 구매했습니다.");
        for (int i = 0; i < ticketAmount; i++) {
            System.out.println(lottos.get(i));
        }
    }
    public static void getInput(){
        System.out.println("\n당첨 번호를 입력해주세요.");
        inputNumbers = Console.readLine().split(",");
    }

    private static void validateNumber(String[] input){
        if(input.length != 6)
            throw new IllegalArgumentException("[ERROR] 6개의 숫자가 아님");
    }
    public static void getBonusNumber(){
        System.out.println("\n보너스 번호를 입력해주세요.");
        BonusNumber = Integer.parseInt(Console.readLine());
    }
    public static List<Integer> input2WinnigNumber(String[] inputNumbers){
        for (String number : inputNumbers) {
            int num = Integer.parseInt(number);
            winningNumbers.add(num);
        }
        return winningNumbers;
    }
    static boolean validateRange(){
        for (int num : winningNumbers ) {
            if(num < 1 && num > 45)
                isRightRange = false;
        }
        return isRightRange;
    }
    private static boolean checkOverlappedNumber(){
        for (int i = 0; i < winningNumbers.size(); i++) {
            int overlapTime = Collections.frequency(winningNumbers,winningNumbers.get(i));
            if (overlapTime > 1)
                overlapExist = true;
        }
        return overlapExist;
    }

    static void occurException(){
        if (overlapExist)
            throw new IllegalArgumentException("[ERROR] 중복 숫자 존재");
        if(isRightRange == false)
            throw new IllegalArgumentException("[ERROR] 1 ~ 45 이내 범위가 아닌 숫자가 있음");
    }

    static void compareNumbers(List<Integer> lotto){
        for (int i = 0; i < winningNumbers.size(); i++) {
            if(Collections.frequency(lotto,winningNumbers.get(i)) != 0) //  i번째 로또에 당첨번호의 요소가 있는가
                matchRecord += 1;
        }
    }

    public static void getLottoRecord(){
        for (int i = 0; i < lottos.size(); i++) {
            // 당첨 일치 개수 구하기
            compareNumbers(lottos.get(i));
            lottoRecord[i] = matchRecord;
            matchRecord = 0;
        }
    }

    public static void main(String[] args) {
        // TODO: 프로그램 구현

        // 구입 금액 입력 받기
        getLotteryTickets(getMoney(),PRICE);
        checkValidatePrice(purchasePrice,PRICE);

        //  로또 생성
        createLotto();
        //  로또 출력
        printLottoNumbers();

        // 당첨 번호 입력 받기 : 쉼표 기준으로 구분
        getInput();
        // 보너스 번호 입력
        getBonusNumber();

        input2WinnigNumber(inputNumbers);
        validateNumber(inputNumbers);
        validateRange();
        checkOverlappedNumber();;

        // 예외처리
        occurException();

        // 로또 통계
        getLottoRecord();

        Grade grade;
        //  당첨 유형 분류
        for (int i = 0; i < lottos.size(); i++) {
            if (lottoRecord[i] == 3)
                grade = Grade.fifth;
            if (lottoRecord[i] == 4)
                grade = Grade.fourth;
            if (lottoRecord[i] == 5)
                grade = Grade.third;
            if (lottoRecord[i] == 5 && lottos.get(i).contains(BonusNumber))
                grade = Grade.second;
            if (lottoRecord[i] == 6)
                grade = Grade.first;
        }


        // 당첨 내역에 따른 상금 수여



        /*
        try{
        }catch(PatternSyntaxException e) {
        }
        */
    }
}
