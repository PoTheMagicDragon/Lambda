package com.lambda.forge;

import com.lambda.LambdaMod;
import net.minecraftforge.fml.common.Mod;

@Mod("lambda")
public class ForgeLambdaMod {
    ForgeLambdaMod() {
        System.out.println("Hello From Java Lambda Forge!");
        new LambdaMod().start();
    }
}
