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

import com.asiainfo.utils.aelstad.keccakj.io.BitOutputStream;

import java.security.MessageDigest;

public abstract class AbstractKeccackMessageDigest extends MessageDigest {
	
	KeccackSponge keccackSponge;
	BitOutputStream absorbStream;
	int digestLength;

	/**
	 * Security level in bits is min(capacity/2,digestLength*8). 
	 * 
	 * @param algorithm Algorithm name
	 * @param capacity Keccack capacity in bits. Must be a multiple of 8.
	 * @param digestLength Length of digest in bytes
	 * @param domainPadding 
	 * @param domainPaddingBits 
	 */
	public AbstractKeccackMessageDigest(String algorithm, int capacityInBits, int digestLength, byte domainPadding, int domainPaddingBitLength)
	{
		super(algorithm);
		this.keccackSponge = new KeccackSponge(capacityInBits, domainPadding, domainPaddingBitLength);
		
		this.absorbStream = keccackSponge.getAbsorbStream();
		this.digestLength = digestLength;
	}

}
