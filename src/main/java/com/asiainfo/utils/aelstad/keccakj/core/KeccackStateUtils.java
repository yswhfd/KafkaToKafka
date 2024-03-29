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


/**
 * Contains methods to manipulate Keccack 64-bit longs state using various-length primitvies
 * 
 */
final class KeccackStateUtils {
		
	public enum StateOp {
		ZERO, GET, VALIDATE, XOR_IN, XOR_TRANSFORM, WRAP, UNWRAP;
		
		public boolean isIn() {
			return (this == StateOp.XOR_IN || this == XOR_TRANSFORM || this == StateOp.WRAP || this == StateOp.UNWRAP || this == VALIDATE);
		}
		
		public boolean isOut() {
			return (this == StateOp.GET || this == XOR_TRANSFORM || this== StateOp.WRAP || this == StateOp.UNWRAP);
		}

	};
	
	public static long longOp(StateOp stateOp, long[] state, int pos, long val, int bitoff, int bitlen) {
		long mask;
		if(bitlen < 64) {
			mask = ~(~0l << bitlen);
			mask = mask << bitoff;
		} else {
			mask = ~0l;
		}
		
		long rv = 0;
		long stateval = state[pos];
		switch (stateOp) {
		case ZERO:
			state[pos] = stateval^(stateval&mask);
			break;
		case GET:
			rv = stateval & mask;
			break;
		case XOR_TRANSFORM:
			rv = (val ^ stateval) & mask;
			break;
		case XOR_IN:
			stateval = stateval ^ (val & mask);
			state[pos] = stateval;
			break;
		case VALIDATE:
			rv = (val ^ stateval) & mask;
			break;
		case UNWRAP:
			rv = (val ^ stateval) & mask;
			stateval = stateval ^ rv;
			state[pos] = stateval;
			break;
		case WRAP:
			rv = (val ^ stateval) & mask;
			stateval = stateval ^ (val & mask);
			state[pos] = stateval;
			break;
		}
		return rv;
	}

		
	public static long longOp(StateOp stateOp, long[] state, int pos, long val) {
		return longOp(stateOp, state, pos, val, 0, 64);
	}
	

	public static byte byteOp(StateOp stateOp, long[] state, int pos, byte val) {
		int lpos = pos >> 3;
		int bitoff = (pos & 7) << 3;
		
		return (byte) ((longOp(stateOp, state, lpos, ((long)val)<<bitoff, bitoff, 8) >>> bitoff) & 0xffl);  		
	}
	
	public static boolean bitOp(StateOp stateOp, long[] state, int pos, boolean val) {
		int lpos = pos >> 6;
		int bitoff = (pos & 63);
		
		return ((longOp(stateOp, state, lpos, (val ? 1l : 0l)<<bitoff, bitoff, 1) >>> bitoff) & 1l)==1l;  		
	}

	public static void bytesOp(StateOp stateOp, long[] state, int pos,
			byte[] out, int outpos, byte[] in, int inpos, int len)
	{			
		if(len > 7 && (len&7)==0 && (pos&7)==0) {
			long invalid = 0;
			boolean isIn = stateOp.isIn();
			boolean isOut = stateOp.isOut();
			long mask = 0xff;
			do {
				long tmp = 0;
				if(isIn) {
					tmp = ((long) in[inpos]) & mask;
					++inpos;
					tmp |= (((long) in[inpos]) & mask)<<8;
					++inpos;
					tmp |= (((long) in[inpos]) & mask)<<16;
					++inpos;
					tmp |= (((long) in[inpos]) & mask)<<24;
					++inpos;
					tmp |= (((long) in[inpos]) & mask)<<32;
					++inpos;
					tmp |= (((long) in[inpos]) & mask)<<40;
					++inpos;
					tmp |= (((long) in[inpos]) & mask)<<48;
					++inpos;
					tmp |= (((long) in[inpos]) & mask)<<56;
					++inpos;														
				}
				tmp = longOp(stateOp, state, pos>>3, tmp);
				if(isOut) {
					out[outpos] = (byte) (tmp & mask);
					++outpos;
					tmp >>>= 8;
					out[outpos] = (byte) (tmp & mask);
					++outpos;
					tmp >>>= 8;
					out[outpos] = (byte) (tmp & mask);
					++outpos;
					tmp >>>= 8;
					out[outpos] = (byte) (tmp & mask);
					++outpos;
					tmp >>>= 8;
					out[outpos] = (byte) (tmp & mask);
					++outpos;
					tmp >>>= 8;
					out[outpos] = (byte) (tmp & mask);
					++outpos;
					tmp >>>= 8;
					out[outpos] = (byte) (tmp & mask);
					++outpos;
					tmp >>>= 8;
					out[outpos] = (byte) (tmp & mask);
					++outpos;
				}
				if(stateOp == StateOp.VALIDATE) {
					invalid |= tmp;
				}					
				
				pos += 8;
				len -= 8;
			} while(len > 0);
			if(stateOp == StateOp.VALIDATE && invalid != 0) {
				throw new KeccackStateValidationFailedException();
			}					
		} else {
			bitsOp(stateOp, state, pos<<3, out, ((long) outpos)<<3, in, ((long)inpos)<<3, len <<3);
		}
	}

	public static byte bitsOp(StateOp stateOp, long[] state, int pos, byte in, int len)
	{
		boolean isIn = stateOp.isIn();
		boolean isOut = stateOp.isOut();
		byte rv=0;
		
		int lpos = (pos>>6);
		int loff = (pos & 63);
		
		
		int len1 = Math.min(len, 64-loff);
		int len2 = len - len1;
		long mask1 = ((~(0xff<<len1))&0xffl);
		long mask2 = ((~(0xff<<len2))&0xffl);
		
		long tmp = 0;
		if(isIn) {
			tmp = ((long) in) & mask1;
			tmp <<= loff;			
		}
		tmp = longOp(stateOp, state, lpos, tmp, loff, len1);
		if(isOut) {
			tmp >>= loff;
			rv = (byte) (tmp & mask1);
		}
		
		if(len2 > 0) {
			++lpos;
			loff = 0;

			if(isIn) {
				tmp = ((long) (in>>>len1)) & mask2;
			}
			tmp = longOp(stateOp, state, lpos, tmp, loff, len2);
			if(isOut) {
				rv |= ((byte) (tmp & mask2))<<len1;
			}
		}
		
		return rv;
	}

	
	public static void bitsOp(StateOp stateOp, long[] state, int pos,
			byte[] out, long outpos, byte[] in, long inpos, int len) 
	{
		long invalid=0;
		boolean isIn = stateOp.isIn();
		boolean isOut = stateOp.isOut();
		while(len > 0) {
			int bitoff = pos & 63;
			int bitlen = Math.min(64 - bitoff, len);			
			
			long tmp = 0;
			int lpos = pos >> 6;
		
			if(isIn) {
				tmp = setBitsInLong(in, inpos, tmp, bitoff, bitlen);
				inpos += bitlen;
			}
			tmp = longOp(stateOp, state, lpos, tmp, bitoff, bitlen);
			if(isOut) {
				setBitsFromLong(out, outpos, tmp, bitoff, bitlen);
				outpos += bitlen;
			}
			if(stateOp == StateOp.VALIDATE) {
				invalid |= tmp;
			}
			pos += bitlen;
			bitoff += bitlen;
			len -= bitlen;
		}
		if(stateOp == StateOp.VALIDATE && invalid != 0) {
			throw new KeccackStateValidationFailedException();
		}
	}
	
	static long setBitsInLong(byte[] src, long srcoff,  long l, int off, int len)
	{
		int shift=off;
		// clear bits in l
		long mask = ~(~0l << len);
		mask = mask << off;
		l ^= l & mask;
		while(len > 0) {
			int bitoff = (int) (srcoff & 7);
			int srcByteOff = (int) (srcoff >> 3);
			if(bitoff==0 && len >= 8) {
				do {
					// aligned byte
					long val = ((long )(src[srcByteOff])) &0xffl; 

					l |= val << shift;
					shift += 8;
					len -= 8;
					srcoff += 8;
					++srcByteOff;
				} while(len >= 8);			
			} else {
				int bitlen = Math.min(8 - bitoff, len);
				
				byte valmask = (byte) ((0xff << bitoff) & (0xff >>> (8-bitlen-bitoff)));
				long lval = ((long )(src[srcByteOff] & valmask)) & 0xffl;
				lval >>>= bitoff;

				l |= lval << shift;
				
				srcoff += bitlen;
				len -= bitlen;
				shift += bitlen;								
			}
		}		
		return l;		 
	}
	
	static void setBitsFromLong(byte[] dst, long dstoff,  long l, int off, int len)
	{
		int shift=off;
		while(len > 0) {
			int bitoff = (int) dstoff & 7;
			int dstByteOff = (int) (dstoff >> 3);

			if(bitoff==0 && len >= 8) {
				do {
					// aligned byte
					dst[dstByteOff] = (byte) ((l >>> shift) & 0xff);
					shift += 8;
					len -= 8;
					dstoff += 8;
					++dstByteOff;
				} while(len >= 8);			
			} else {				
				int bitlen = Math.min(8 - bitoff, len);
				byte mask = (byte) ((0xff << bitoff) & (0xff >>> (8-bitlen-bitoff)));				
				byte val = dst[dstByteOff];
				long lval = (l >>> shift); 
				
				val ^= val & mask;
				val |= (lval<<bitoff) & mask;
				
				dst[dstByteOff] = val;
												
				dstoff += bitlen;
				len -= bitlen;
				shift += bitlen;				
			}
		}		
	}	
	
}
