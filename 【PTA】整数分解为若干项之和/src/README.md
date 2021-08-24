# 【PTA】整数分解为若干项之和

## 题目描述

将一个正整数N分解成几个正整数相加，可以有多种分解方法，例如7=6+1，7=5+2，7=5+1+1，…。编程求出正整数N的所有整数分解式子。

**输入格式**

每个输入包含一个测试用例，即正整数N (0<N≤30)。

**输出格式**

按递增顺序输出N的所有整数分解式子。递增顺序是指：对于两个分解序列N1={n1,n2,⋯}和N2={m1,m2,⋯}，若存在i使得n1=m1，⋯，ni=mi，但是ni+1<mi+1，则N1序列必定在N2序列之前输出。每个式子由小到大相加，式子间用分号隔开，且每输出4个式子后换行。

**输入样例**

```in
7
```

**输出样例**

```out
7=1+1+1+1+1+1+1;7=1+1+1+1+1+2;7=1+1+1+1+3;7=1+1+1+2+2
7=1+1+1+4;7=1+1+2+3;7=1+1+5;7=1+2+2+2
7=1+2+4;7=1+3+3;7=1+6;7=2+2+3
7=2+5;7=3+4;7=7
```

## 测试点

| 测试点 |        描述        |
| :----: | :----------------: |
|   0    | 样例等价，多行输出 |
|   1    |     一行内输出     |
|   2    |       最小N        |
|   3    |       最大N        |

## 思路分析

先根据上面的输出找规律，可以观察到：

- 最多7个数相加（且全是1），最少1个数相加（且为7）。（可以拿一个数组储存这一排数）
- 1+1+5如何变成1+2+2+2的，怎么还变长了？说明式子的长度不是越来越短的，而是1+1+5达到7后，第3个数及之后就不能再加数字了，应该把第2个数加一个（1+2），然后从第3个数开始变化（1+2+2，第3个数不能小于第2个数）。

这就说明这需要用到递归。

先写几个成员变量：

```java
public static int N;
public static int[] numbers = new int[35];
public static int interval = 0;
```

这里`N`用来存储N，`numbers`用来表示一排分解数，`interval`表示公式的个数。

先写个`printItem`的函数，用来格式化分解数。

```java
public static String printItem(int[] array, int pos) {
	String result = N + "=";
	for (int i = 0; i <= pos; i++) {
		result += array[i];
		if (i < pos) result += "+";
	}
	if (interval % 4 == 0 || pos == 0) result += "\n";
	else result += ";";
	return result;
}
```

`pos`表示分解数的位置（即分解数的个数减1），如`pos`为6就表示输出`array[0]`到`array[6]`这7个数。

同时要处理`+`和`;`放的位置，最后得到一条形如`7=1+2+2+2;`的字符串。

然后写主函数：

```java
public static void main(String[] args) {
	Scanner in = new Scanner(System.in);
	N = in.nextInt();
	for (int i = 1; i <= N / 2; i++) {
		dfs(0, 0, i);
	}
	dfs(0, 0, N);
}
```

意思是，第一个数从1到N/2（而非N-1，防止`7=4+3`出现），都要寻找输出公式。再当第一个数为N的时候，单独输出`N=N`。

然后设计函数`dfs`，它有三个参数，分别是：

```java
public static void dfs(int pos, int sum, int next)
```

`pos`表示**当前的处理位置**，sum表示**当前的总和**，next表示**下一个要加的数字**（函数开始时还没加）。所以主函数里面写`dfs(0, 0, i)`。

在加`next`之前，需要先判断一下有没有加的必要，比如到`7=1+5`这一步了，`pos`、`sum`、`next`三个参数分别为2、6、5，但是没有加的必要了。

```java
if (sum + next > N) return;
```

否则，就把这个数加上，也就是把数组当前位置（`pos`）赋值为这个数（`next`）。

```java
else {
    numbers[pos] = next;
}
```

加上之后，再判断一下，现在这一排数到N了没有，如果到了，就可以输出了。

```java
else {
	numbers[pos] = next;
	if (sum + next == N) {
		interval++;
		System.out.print(printItem(numbers, pos));
	}
	...
}
```

如果没到，就需要递归，其中递归的参数中有如下变化：

- `pos`因为当前位置有数字了，所以当前位置应该加1；
- `sum`因为加上当前数字了，所以当前总和应该加`next`；
- `next`比较复杂，应该是多少？

比如现在在`7=1+1+1`这一步，`pos`从2变3，`sum`从2变3，`next`应该是多少？

这个递归肯定不止做一次，应该是个循环，因为在`7=1+1+1`之后，`next`可以是1，可以是2，可以是3，可以是4，不能是5。

也就是说在`7=1+1+1`这一步，应该依次计算`dfs(3, 3, 1)`，`dfs(3, 3, 2)`，`dfs(3, 3, 3)`，`dfs(3, 3, 4)`，从而得出`7=1+1+1+1+1+1+1`、`7=1+1+1+1+1+2`、`7=1+1+1+1+3`，`7=1+1+1+2+2`、`7=1+1+1+4`这5个式子，至于得到`next`后怎么处理得到7，那就是重复上面的步骤，也就是递归。

所以上面省略号部分应该这么写：

```java
else if (sum + next < N) {
	sum += next;
	pos++;
	for (int i = next; i <= N - sum; i++) {
		dfs(pos, sum, i);
	}
}
```

`dfs`的`next`要从当前`next`开始循环（因为下一个数不能小于上一个数），直到`N-sum`为止（总大小为N，已经加到`sum`了，最多再加`N-sum`这么大）。

递归处理完成。

## 完整代码

```java
import java.util.Scanner;

public class Main {
    public static int N;
    public static int[] numbers = new int[35];
    public static int interval = 0;

    public static String printItem(int[] array, int pos) {
        String result = N + "=";
        for (int i = 0; i <= pos; i++) {
            result += array[i];
            if (i < pos) result += "+";
        }
        if (interval % 4 == 0 || pos == 0) result += "\n";
        else result += ";";
        return result;
    }

    public static void dfs(int pos, int sum, int next) {
        if (sum + next > N) return;
        else {
            numbers[pos] = next;
            if (sum + next == N) {
                interval++;
                System.out.print(printItem(numbers, pos));
            } else if (sum + next < N) {
                sum += next;
                pos++;
                for (int i = next; i <= N - sum; i++) {
                    dfs(pos, sum, i);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        N = in.nextInt();
        for (int i = 1; i <= N / 2; i++) {
            dfs(0, 0, i);
        }
        dfs(0, 0, N);
    }
}
```

