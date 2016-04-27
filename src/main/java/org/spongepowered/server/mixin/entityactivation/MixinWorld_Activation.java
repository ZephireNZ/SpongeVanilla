/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.server.mixin.entityactivation;

import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.interfaces.world.IMixinWorld;
import org.spongepowered.common.mixin.plugin.entityactivation.ActivationRange;
import org.spongepowered.common.mixin.plugin.entityactivation.interfaces.IModData_Activation;

@NonnullByDefault
@Mixin(net.minecraft.world.World.class)
public abstract class MixinWorld_Activation implements IMixinWorld {

    @Inject(method = "updateEntityWithOptionalForce", at = @At(value = "FIELD", target = "lastTickPosX:D", opcode = Opcodes.PUTFIELD, ordinal = 0),
            cancellable = true)
    public void onUpdateEntityWithOptionalForce(net.minecraft.entity.Entity entity, boolean forceUpdate, CallbackInfo ci) {
        if (!ActivationRange.checkIfActive(entity)) { // ignore if forced by forge event update or entity's chunk
            entity.ticksExisted++;
            ((IModData_Activation) entity).inactiveTick();
            ci.cancel();
        }
    }

}
