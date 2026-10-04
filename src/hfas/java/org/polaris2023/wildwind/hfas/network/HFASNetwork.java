package org.polaris2023.wildwind.hfas.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.polaris2023.wildwind.hfas.HFASMod;
import org.polaris2023.wildwind.hfas.network.payload.ServerBoundSelectOmniClawItemPayload;

/**
 * 模组网络负载注册入口喵~
 */
@EventBusSubscriber(modid = HFASMod.MOD_ID)
public class HFASNetwork {

	@SubscribeEvent
	static void register(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.commonToServer(
				ServerBoundSelectOmniClawItemPayload.TYPE,
				ServerBoundSelectOmniClawItemPayload.STREAM_CODEC,
				ServerBoundSelectOmniClawItemPayload::handle
		);
	}

	private HFASNetwork() {
	}
}