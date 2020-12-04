package com.web.blog.service;

public class MEDCompute {
    public static int Compute_MED(String target, String origin) {
        System.out.println("내가 받은 단어 : " + target);
        System.out.println("내가 보정하려는 타겟 단어 : " + origin);
        char target_jaso[] = hangulToJaso(target);
        char origin_jaso[] = hangulToJaso(origin);

        int target_size = target_jaso.length;
        int origin_size = origin_jaso.length;
        int MED[][] = new int[target_size][origin_size];

        // 전처리
        for (int i = 0; i < target_size; i++)
            MED[i][0] = i;
        for (int i = 0; i < origin_size; i++)
            MED[0][i] = i;

        for (int i = 1; i < target_size; i++) {
            for (int j = 1; j < origin_size; j++) {
                // 실제 여기서는 자소배열인 char 배열의 index로 접근 O(1)
                if (target_jaso[i] == origin_jaso[j]) {
                    MED[i][j] = MED[i - 1][j - 1];
                } else {
                    MED[i][j] = Math.min(Math.min(MED[i - 1][j - 1], MED[i - 1][j]), MED[i][j - 1]) + 1;
                }
            }
        }
        return MED[target_size - 1][origin_size - 1];
    }

    final static char[] ChoSung = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145,
            0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };
    final static char[] JwungSung = { 0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158,
            0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163 };
    final static char[] JongSung = { 0, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b,
            0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a,
            0x314b, 0x314c, 0x314d, 0x314e };

    public static char[] hangulToJaso(String s) {

        int a, b, c;
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            // 글자가 "가" -"힣" 일때만 분석
            if (ch >= 0xAC00 && ch <= 0xD7A3) {
                c = ch - 0xAC00;
                a = c / (21 * 28);
                c = c % (21 * 28);
                b = c / 28;
                c = c % 28;

                result = result + ChoSung[a] + JwungSung[b];

                if (c != 0)
                    result = result + JongSung[c];
            } else {
                result = result + ch;
            }
        }
        return result.toCharArray();
    }
}