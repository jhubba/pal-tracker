package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private String port;
    private String memory;
    private String index;
    private String address;


    public EnvController(@Value("${port:NOT SET}") String port,
                         @Value("${memory.limit:NOT SET}") String memory,
                         @Value("${cf.instance.index:NOT SET}") String index,
                         @Value("${cf.instance.addr:NOT SET}")String address) {
        this.port = port;
        this.memory = memory;
        this.index = index;
        this.address = address;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {

        Map<String, String> map = Map.of(
                "PORT", port,
                "MEMORY_LIMIT", memory,
                "CF_INSTANCE_INDEX", index,
                "CF_INSTANCE_ADDR", address
        );

        return map;

    }
}
