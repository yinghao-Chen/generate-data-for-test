package cn.cyh.generatedata.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author cyh
 * @date 2022/11/11
 */
public class SfzhUtil {

    private SfzhUtil() {}

    /**
     * 身份证前两位对应的身份
     */
    private static final Map<Integer, String> PROVS_MAP = new HashMap<Integer, String>(16) {{
        put(11, "北京");  put(12, "天津");  put(13, "河北");  put(14, "山西");  put(15, "内蒙古");
        put(21, "辽宁");  put(22, "吉林");  put(23, "黑龙江");
        put(31, "上海");  put(32, "江苏");  put(33, "浙江");  put(34, "安徽");  put(35, "福建");  put(36, "江西");  put(37, "山东");
        put(41, "河南");  put(42, "湖北");  put(43, "湖南");  put(44, "广东");  put(45, "广西");  put(46, "海南");
        put(50, "重庆");  put(51, "四川");  put(52, "贵州");  put(53, "云南");  put(54, "西藏");
        put(61, "陕西");  put(62, "甘肃");  put(63, "青海");  put(64, "宁夏");  put(65, "新疆");
        put(71, "台湾");  put(81, "香港");  put(82, "澳门");
    }};
    private static final String[] CHECK_OPTIONS = new String[]{"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "51", "52", "53", "54", "50", "61", "62", "63", "64", "65", "71", "81", "82"};

    /**
     * 身份证前17位因子
     * length=17
     */
    private static final int[] FACTOR = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 第18位与11取模对应值
     */
    private static final String[] PARITY = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    public static String generateSfzh() {
        int random = new Random().nextInt(10);
        
        return "";
    }

    /**
     *         handleCheckAllChange(val) {
     *             this.checkList = val ? checkOptions : []
     *             this.isIndeterminate = false
     *         },
     *         handleCheckedCitiesChange(value) {
     *             let checkedCount = value.length
     *             this.checkAll = checkedCount === checkOptions.length
     *             this.isIndeterminate = checkedCount > 0 && checkedCount < this.checkList.length
     *         },
     *         changeNum(num) {
     *             console.info(num)
     *         },
     *         btn1() {
     *             const pro1 = Object.keys(provs)
     *             const { checkList } = this
     *             const pro = pro1.filter(d => checkList.includes(d))
     *             if (pro.length === 0) {
     *                 return
     *             }
     *             const arr = []
     *             for (let i = 0; i < this.input4; i++) {
     *                 const sfzh = String(pro[this.getRandom(0, pro.length - 1)]) + this.prefixInteger(this.getRandom(1, 9999), 4) +
     *                     String(this.getRandom(70, 99)) + String(this.randomDate().substring(4)) +
     *                     this.prefixInteger(this.getRandom(1, 999), 3)
     *                 arr.push(sfzh)
     *             }
     *             this.sfzhRdm = arr.join(',')
     *         },
     *         randomDate() {
     *             const maxdaterandom = new Date().getTime()
     *             // 由于当前环境为北京GMT+8时区，所以与GMT有8个小时的差值
     *             const mindaterandom = new Date(1970, 0, 1, 8).getTime()
     *             const randomdate = this.getRandom(mindaterandom, maxdaterandom)
     *             return this.$moment(randomdate).format('YYYYMMDD')
     *         },
     *         getRandom(min, max) {
     *             min = Math.ceil(min)
     *             max = Math.floor(max)
     *             return Math.floor(Math.random() * (max - min + 1)) + min
     *         },
     *         btn2() {
     *             const pro1 = Object.keys(provs)
     *             const { checkList } = this
     *             const pro = pro1.filter(d => checkList.includes(d))
     *             if (pro.length === 0) {
     *                 return
     *             }
     *             const arr = []
     *             for (let i = 0; i < this.input4; i++) {
     *                 const sfzh17 = String(pro[this.getRandom(0, pro.length - 1)]) + this.prefixInteger(this.getRandom(1, 9999), 4) +
     *                     String(this.randomDate()) + this.prefixInteger(this.getRandom(1, 999), 3)
     *                 let sum = 0
     *                 for (let i = 0; i < 17; i++) {
     *                     sum += sfzh17[i] * factor[i]
     *                 }
     *                 arr.push(sfzh17 + parity[sum % 11])
     *             }
     *             this.sfzhRdm = arr.join(',')
     *         },
     *         prefixInteger(num, length) {
     *             return (Array(length).join('0') + num).slice(-length)
     *         },
     *         changeID(val) {
     *             this.len_18_value = ''
     *             this.len_18_value_error = ''
     *             if (!val) {
     *                 return
     *             }
     *             if (!this.checkID1 || !this.checkDate('19' + val.substring(6, 12)) || !this.checkProv(val.substring(0, 2))) {
     *                 this.len_18_value_error = '请输入正确的15位身份证号'
     *             }
     *             if (val) {
     *                 if (val.length !== 15) {
     *                     this.len_18_value = ''
     *                     this.len_18_value_error = '请输入15位的身份证号'
     *                 } else {
     *                     const addVal = val.substring(0, 6) + '19' + val.substring(6)
     *                     const factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
     *                     const parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ]
     *                     let sum = 0
     *                     for (let i = 0; i < 17; i++) {
     *                         sum += addVal[i] * factor[i]
     *                     }
     *                     this.len_18_value = addVal + parity[sum % 11]
     *                     this.len_18_value_error = ''
     *                 }
     *             }
     *         },
     *         **/
}
