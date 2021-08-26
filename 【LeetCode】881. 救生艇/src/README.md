# 【LeetCode】881. 救生艇

## 题目描述

第`i`个人的体重为`people[i]`，每艘船可以承载的最大重量为`limit`。

每艘船最多可同时载两人，但条件是这些人的重量之和最多为`limit`。

返回载到每一个人所需的最小船数。（保证每个人都能被船载）。

**示例1**

```
输入：people = [1, 2], limit = 3
输出：1
解释：1 艘船载 (1, 2)
```

**示例2**

```
输入：people = [3, 2, 2, 1], limit = 3
输出：3
解释：3 艘船载 (1, 2), (2) 和 (3)
```

**示例3**

```
输入：people = [3, 5, 3, 4], limit = 5
输出：4
解释：4 艘船载 (3), (3), (4), (5)
```

**提示**

- 1 <= `people.length` <= 50000
- 1 <= `people[i]` <= `limit` <= 30000

## 测试点

78个测试点

## 思路分析

显然数组是需要排序的，先`sort`一波，并创建变量`boat`用来返回结果。

```java
Arrays.sort(people);
int boat = 0;
...
return boat;
```

一艘船可以一个人走，可以两个人走，想让船的总数最少，应让两个人走的船最多。

如果从体重最重的人开始考虑：

第1艘船让**体重最重**的人先上船，会有以下结果：

- 这个船不能再上人了（连**最轻**的人都上不去了），于是这艘船判定结束，问题规模减一；
- 还能再上一个，这时该上谁？

如果此时还能上n个人，这n个人显然在数组的左边（最轻的一直到比较轻的），从这里挑一个人。

看起来好像挑体重比较轻的比较合适，但实际上，既然自己是最重的，那这个比较轻的人可以跟着其他任何一个人走。我们选**体重最轻**的那个人就可以了。

或者我们换一个角度，从体重最轻的人开始考虑：

第1艘船让**体重最轻**的人先上船，会有以下结果：

- **体重最重**的人不能上船，那这个体重最重的人不能跟任何一个人上船，他只能一人一艘船，问题规模减一；
- **体重最重**的人能上船，那这个体重最轻的人可以跟任何一个人上船，那让他把体重最重的人带走是最好的，问题规模减二。

因此每次只需要判断**体重最轻的和最重的**人就可以了，他俩或者最重的走后，把船加一个，把人去掉，再重复上面的判断。

数组已经排序好了，没有必要去删除数组元素，判断角标就可以了，以及考虑一下剩下一个人的情况。

```java
int left = 0;
int right = people.length - 1;
while (left <= right) {
    if (left == right) {
        boat++; // 最后一个人，直接走
        break;
    }
    if (people[left] + people[right] <= limit) {
        left++;
        right--;
        boat++; // 最轻的和最重的一起走了
    } else {
        right--;
        boat++; // 最重的人走了
    }
}
```

但实际上上面的while循环还可以化简，可以把相同部分省略，并删去判断剩下一个人的情况。

```java
while (left <= right) {
    if (people[left] + people[right] <= limit) {
        left++;
    }
    right--;    // 无论最轻的走不走，最重的肯定走
    // 剩一个人时，最重的就是他自己，而且肯定会break
    boat++;
}
```

完成。

## 完整代码

此处只展示LeetCode的答案部分，不展示调试部分。

```java
class Solution {
    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int boat = 0;
        int left = 0;
        int right = people.length - 1;
        while (left <= right) {
            if (people[left] + people[right] <= limit) {
                left++;
            }
            right--;
            boat++;
        }
        return boat;
    }
}
```

