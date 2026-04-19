package com.example;

import java.util.function.Function;

/**
 * Email:1058051552@qq.com
 * Date 2022-8-1 23:28
 * Description 代码版权即所在公司及个人所有
 *
 * @author tianshaofei
 */
public class model {
    static class X{
        String f(){
            return "X::f()";
        }
    }

    interface MakeString{
        String make();
    }

    interface TransformX{
        String transform(X x);
    }

    static class test{
        public static void main(String[] args) {
            TransformX t = X::f;
            X x = new X();
            System.out.println(t.transform(x));
            System.out.println(x.f());
        }
    }
}
