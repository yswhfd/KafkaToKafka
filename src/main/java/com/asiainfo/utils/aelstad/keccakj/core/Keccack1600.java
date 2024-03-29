/*
 * Copyright 2014 Amund Elstad. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.asiainfo.utils.aelstad.keccakj.core;

import java.util.Arrays;

/**
 * Java port of the reference implementation of Keccack-1600 permuation 
 * from https://github.com/gvanas/KeccakCodePackage
 *
 */
public final class Keccack1600 {
	
	public Keccack1600()
	{
		this(256, 24);
	}
	
	public Keccack1600(int capacitityInBits) {
		this(capacitityInBits, NR_ROUNDS);
	}
	
	public Keccack1600(int capacityInBits, int rounds) {	
		this.capacitityBits = capacityInBits;
		this.rateBits = 1600-capacityInBits;
		this.rateBytes = rateBits >> 3;
		this.firstRound = NR_ROUNDS - rounds;
		
		clear();
	}
	
	byte byteOp(KeccackStateUtils.StateOp stateOp, int stateByteOff, byte in)
	{
		if(stateByteOff >= rateBytes)
			throw new IndexOutOfBoundsException();
		
		return KeccackStateUtils.byteOp(stateOp, state, stateByteOff, in); 		
	}
	
	
	void bytesOp(KeccackStateUtils.StateOp stateOp, int stateByteOff, byte[] out, int outpos, byte[] in, int inpos, int lenBytes)
	{
		if(stateByteOff+lenBytes > rateBytes)
			throw new IndexOutOfBoundsException();
		
		KeccackStateUtils.bytesOp(stateOp, state, stateByteOff, out, outpos, in, inpos, lenBytes);		
	}
	
	void bitsOp(KeccackStateUtils.StateOp stateOp, int stateBitOff, byte[] out, long outpos, byte[] in, long inpos, int lenBits)
	{
		if(stateBitOff+lenBits > rateBits)
			throw new IndexOutOfBoundsException();
		
		KeccackStateUtils.bitsOp(stateOp, state, stateBitOff, out, outpos, in, inpos, lenBits);		
	}

	
	public void getBytes(int stateByteOff, byte[] buf, int bufByteOff, int lenBytes) {
		bytesOp(KeccackStateUtils.StateOp.GET, stateByteOff, buf, bufByteOff, null, 0, lenBytes);
	}
	
		
	public void setXorByte(int stateByteOff, byte val) {
		byteOp(KeccackStateUtils.StateOp.XOR_IN, stateByteOff, val);
	}

	
	public void setXorBytes(int stateByteOff, byte[] buf, int bufByteOff, int lenBytes) {
		bytesOp(KeccackStateUtils.StateOp.XOR_IN, stateByteOff, null, 0, buf, bufByteOff, lenBytes);
	}
	
	
	public void zeroBytes(int stateByteOff, int lenBytes) {
		bytesOp(KeccackStateUtils.StateOp.ZERO, stateByteOff, null, 0, null, 0, lenBytes);
	}
	
	
	public void getBits(int stateBitOff, byte[] buf, long bufBitOff, int lenBits) {
		bitsOp(KeccackStateUtils.StateOp.GET, stateBitOff, buf, bufBitOff, null, 0, lenBits);
	}
	
	public final void setXorBits(int stateBitOff, byte[] buf, long bufBitOff, int lenBits) {
		bitsOp(KeccackStateUtils.StateOp.XOR_IN, stateBitOff, null, 0, buf, bufBitOff, lenBits);
	}
		
	public void zeroBits(int stateBitOff, int lenBits) {
		bitsOp(KeccackStateUtils.StateOp.ZERO, stateBitOff, null, 0, null, 0, lenBits);
	}
	
	public void validateBits(int stateBitOff, byte[] buf, int bufBitOff, int lenBits) {
		bitsOp(KeccackStateUtils.StateOp.VALIDATE, stateBitOff, null, 0, buf, bufBitOff, lenBits);
	}
	
	public void wrapBits(int stateBitOff, byte[] outBuf, int outBufOff, byte[] inBuf, int inBufOff, int lenBits)  {
		bitsOp(KeccackStateUtils.StateOp.WRAP, stateBitOff, outBuf, outBufOff, inBuf, inBufOff, lenBits);
	}
	
	public void unwrapBits(int stateBitOff, byte[] outBuf, int outBufOff, byte[] inBuf, int inBufOff, int lenBits) {
		bitsOp(KeccackStateUtils.StateOp.UNWRAP, stateBitOff, outBuf, outBufOff, inBuf, inBufOff, lenBits);
	}

	public int remainingLongs(int longOff) {
		return remainingBits(longOff << 6) >> 6;
	}

	
	public int remainingBytes(int byteOff) {
		return remainingBits(byteOff << 3) >> 3;
	}
					
	public int remainingBits(int bitOff) {
		return rateBits - bitOff;
	}
	
	
	public void pad(byte domainBits, int domainBitLength, int bitPosition) 
	{
		int len = rateBits - bitPosition;
		
		if(len < 0 || domainBitLength>=7)
			throw new IndexOutOfBoundsException();
		
		// add bits for multirate padding
		domainBits |= (1 << domainBitLength);
		++domainBitLength;
		
		boolean multirateComplete  = false;
		// no zeros in multirate padding. add final bit.
		if(len==domainBitLength+1) {
			domainBits |= (1 << domainBitLength);
			++domainBitLength;			
			multirateComplete = true;
		}
		
		while(domainBitLength > 0) {
			int chunk = Math.min(len, domainBitLength);
			if(chunk == 0) {
				permute();
				len = rateBits;
				bitPosition = 0;
				continue;
			}
			KeccackStateUtils.bitsOp(KeccackStateUtils.StateOp.XOR_IN, state, bitPosition, domainBits, chunk);
			
			len -= chunk;
			domainBits >>= chunk;
			domainBitLength -= chunk;
			bitPosition += chunk;
		}
		if(!multirateComplete) {
			if(len == 0) {
				permute();				
			}
			KeccackStateUtils.bitOp(KeccackStateUtils.StateOp.XOR_IN, state, rateBits-1, true);
		}
	}	
	
	
			
	public void permute()
	{
/*
  for (int i=firstRound; i < NR_ROUNDS; ++i) {
			theta();
			rho();
			pi();
			chi();
			iota(i);
	}
*/
		long out0 = state[0];
		long out1 = state[1];
		long out2 = state[2];
		long out3 = state[3];
		long out4 = state[4];
		long out5 = state[5];
		long out6 = state[6];
		long out7 = state[7];
		long out8 = state[8];
		long out9 = state[9];
		long out10 = state[10];
		long out11 = state[11];
		long out12 = state[12];
		long out13 = state[13];
		long out14 = state[14];
		long out15 = state[15];
		long out16 = state[16];
		long out17 = state[17];
		long out18 = state[18];
		long out19 = state[19];
		long out20 = state[20];
		long out21 = state[21];
		long out22 = state[22];
		long out23 = state[23];
		long out24 = state[24];
		for (int i=firstRound; i < NR_ROUNDS; ++i) {
		// Theta
		long c0 = out0;
		long c1 = out1;
		long c2 = out2;
		long c3 = out3;
		long c4 = out4;
		c0 ^= out5;
		c1 ^= out6;
		c2 ^= out7;
		c3 ^= out8;
		c4 ^= out9;
		c0 ^= out10;
		c1 ^= out11;
		c2 ^= out12;
		c3 ^= out13;
		c4 ^= out14;
		c0 ^= out15;
		c1 ^= out16;
		c2 ^= out17;
		c3 ^= out18;
		c4 ^= out19;
		c0 ^= out20;
		c1 ^= out21;
		c2 ^= out22;
		c3 ^= out23;
		c4 ^= out24;
		long d0 = ((c1  << 1) | (c1 >>> 63 )) ^ c4;
		long d1 = ((c2  << 1) | (c2 >>> 63 )) ^ c0;
		long d2 = ((c3  << 1) | (c3 >>> 63 )) ^ c1;
		long d3 = ((c4  << 1) | (c4 >>> 63 )) ^ c2;
		long d4 = ((c0  << 1) | (c0 >>> 63 )) ^ c3;
		out0 = out0 ^ d0;
		out1 = out1 ^ d1;
		out2 = out2 ^ d2;
		out3 = out3 ^ d3;
		out4 = out4 ^ d4;
		out5 = out5 ^ d0;
		out6 = out6 ^ d1;
		out7 = out7 ^ d2;
		out8 = out8 ^ d3;
		out9 = out9 ^ d4;
		out10 = out10 ^ d0;
		out11 = out11 ^ d1;
		out12 = out12 ^ d2;
		out13 = out13 ^ d3;
		out14 = out14 ^ d4;
		out15 = out15 ^ d0;
		out16 = out16 ^ d1;
		out17 = out17 ^ d2;
		out18 = out18 ^ d3;
		out19 = out19 ^ d4;
		out20 = out20 ^ d0;
		out21 = out21 ^ d1;
		out22 = out22 ^ d2;
		out23 = out23 ^ d3;
		out24 = out24 ^ d4;
		// RHO AND PI
		long piOut0 = out0;
		long piOut16 = ((out5  << 36) | (out5 >>> 28 ));
		long piOut7 = ((out10  << 3) | (out10 >>> 61 ));
		long piOut23 = ((out15  << 41) | (out15 >>> 23 ));
		long piOut14 = ((out20  << 18) | (out20 >>> 46 ));
		long piOut10 = ((out1  << 1) | (out1 >>> 63 ));
		long piOut1 = ((out6  << 44) | (out6 >>> 20 ));
		long piOut17 = ((out11  << 10) | (out11 >>> 54 ));
		long piOut8 = ((out16  << 45) | (out16 >>> 19 ));
		long piOut24 = ((out21  << 2) | (out21 >>> 62 ));
		long piOut20 = ((out2  << 62) | (out2 >>> 2 ));
		long piOut11 = ((out7  << 6) | (out7 >>> 58 ));
		long piOut2 = ((out12  << 43) | (out12 >>> 21 ));
		long piOut18 = ((out17  << 15) | (out17 >>> 49 ));
		long piOut9 = ((out22  << 61) | (out22 >>> 3 ));
		long piOut5 = ((out3  << 28) | (out3 >>> 36 ));
		long piOut21 = ((out8  << 55) | (out8 >>> 9 ));
		long piOut12 = ((out13  << 25) | (out13 >>> 39 ));
		long piOut3 = ((out18  << 21) | (out18 >>> 43 ));
		long piOut19 = ((out23  << 56) | (out23 >>> 8 ));
		long piOut15 = ((out4  << 27) | (out4 >>> 37 ));
		long piOut6 = ((out9  << 20) | (out9 >>> 44 ));
		long piOut22 = ((out14  << 39) | (out14 >>> 25 ));
		long piOut13 = ((out19  << 8) | (out19 >>> 56 ));
		long piOut4 = ((out24  << 14) | (out24 >>> 50 ));
		// CHI
		out0 = piOut0 ^ ((~piOut1) & piOut2);
		out1 = piOut1 ^ ((~piOut2) & piOut3);
		out2 = piOut2 ^ ((~piOut3) & piOut4);
		out3 = piOut3 ^ ((~piOut4) & piOut0);
		out4 = piOut4 ^ ((~piOut0) & piOut1);
		out5 = piOut5 ^ ((~piOut6) & piOut7);
		out6 = piOut6 ^ ((~piOut7) & piOut8);
		out7 = piOut7 ^ ((~piOut8) & piOut9);
		out8 = piOut8 ^ ((~piOut9) & piOut5);
		out9 = piOut9 ^ ((~piOut5) & piOut6);
		out10 = piOut10 ^ ((~piOut11) & piOut12);
		out11 = piOut11 ^ ((~piOut12) & piOut13);
		out12 = piOut12 ^ ((~piOut13) & piOut14);
		out13 = piOut13 ^ ((~piOut14) & piOut10);
		out14 = piOut14 ^ ((~piOut10) & piOut11);
		out15 = piOut15 ^ ((~piOut16) & piOut17);
		out16 = piOut16 ^ ((~piOut17) & piOut18);
		out17 = piOut17 ^ ((~piOut18) & piOut19);
		out18 = piOut18 ^ ((~piOut19) & piOut15);
		out19 = piOut19 ^ ((~piOut15) & piOut16);
		out20 = piOut20 ^ ((~piOut21) & piOut22);
		out21 = piOut21 ^ ((~piOut22) & piOut23);
		out22 = piOut22 ^ ((~piOut23) & piOut24);
		out23 = piOut23 ^ ((~piOut24) & piOut20);
		out24 = piOut24 ^ ((~piOut20) & piOut21);
		// IOTA
		out0 ^= KeccackRoundConstants[i];
		}
		state[0] = out0;
		state[1] = out1;
		state[2] = out2;
		state[3] = out3;
		state[4] = out4;
		state[5] = out5;
		state[6] = out6;
		state[7] = out7;
		state[8] = out8;
		state[9] = out9;
		state[10] = out10;
		state[11] = out11;
		state[12] = out12;
		state[13] = out13;
		state[14] = out14;
		state[15] = out15;
		state[16] = out16;
		state[17] = out17;
		state[18] = out18;
		state[19] = out19;
		state[20] = out20;
		state[21] = out21;
		state[22] = out22;
		state[23] = out23;
		state[24] = out24;
	}
		
	public void clear() {
		Arrays.fill(state, 0l);		
	}
		
	
	final static int NR_ROUNDS = 24;
	final static int NR_LANES = 25;
		
	long[] state = new long[NR_LANES];
	
	int rateBytes;
	int rateBits;
	int capacitityBits;
	int firstRound;
		
	final static int index(int x, int y) 
	{
		return (((x)%5)+5*((y)%5));
	}


	final static int[] KeccakRhoOffsets = new int[NR_LANES];
	final static long[] KeccackRoundConstants = new long [NR_ROUNDS];
	
	static {
		KeccakF1600_InitializeRoundConstants();
	    KeccakF1600_InitializeRhoOffsets();
	}
	
	final static void KeccakF1600_InitializeRoundConstants() 
	{
		 byte[] LFSRState= new byte[] { 0x01 } ;
		 int i, j, bitPosition;

		 for(i=0; i < NR_ROUNDS; i++) {
			 KeccackRoundConstants[i] = 0;
			 for(j=0; j<7; j++) {
				 bitPosition = (1<<j)-1; //2^j-1
				 if (LFSR86540(LFSRState))
					 KeccackRoundConstants[i] ^= 1l<<bitPosition;
			 }
		 }
	}
	
	final static boolean LFSR86540(byte[] LFSR)
	{		
	    boolean result = (LFSR[0] & 0x01) != 0;
	    if ((LFSR[0] & 0x80) != 0)
	        // Primitive polynomial over GF(2): x^8+x^6+x^5+x^4+1
	    	LFSR[0] = (byte) ((LFSR[0] << 1) ^ 0x71);
	    else
	    	LFSR[0] <<= 1;
	    return result;
	}	
	
	final static void KeccakF1600_InitializeRhoOffsets() 
	 {
		  int x, y, t, newX, newY;

		  KeccakRhoOffsets[index(0, 0)] = 0;
		  x = 1;
		  y = 0;
		  for(t=0; t<24; t++) {
			  KeccakRhoOffsets[index(x, y)] = ((t+1)*(t+2)/2) % 64;
			  newX = (0*x+1*y) % 5;
			  newY = (2*x+3*y) % 5;
			  x = newX;
			  y = newY;
		  }		
	 }
		
}
