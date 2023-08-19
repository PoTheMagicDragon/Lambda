package com.lambda.fabric;

import com.lambda.LambdaMod;
import net.fabricmc.api.ModInitializer;

public class FabricLambdaMod implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("Hello From Java Lambda Fabric");
        new LambdaMod().start();
    }
}
