package com.arcblock.wallet.appcommonres.view.didmotif

import io.arcblock.walletkit.did.DidUtils
import io.arcblock.walletkit.did.RoleType
import io.arcblock.walletkit.utils.Base58Btc
import io.arcblock.walletkit.utils.address

/**
 * Created by Nate on 2/24/22
 */
object DIDMotifUtils {
  fun getIndexFromDID(did: String): Triple<Int, List<Int>, RoleType> {
    // base58 did -> binary DID string
    // 参考: https://github.com/ArcBlock/ABT-DID-Protocol#create-did (step9 -> step8)
    val decoded = Base58Btc.decode(did.address())
    //println("decoded ${decoded.map { it.toUByte() }}")
    //println("decoded size:${decoded.size}")
    val roleType = DidUtils.decodeDidRoleType(did)
    //println("role type: $roleType")
    // 移除前 2 bytes (did type 部分)
    val striped = decoded.slice(2 until decoded.size)
    //println("striped ${striped.map { it.toUByte() }}")
    //println("striped size:${striped.size}")
    // 前 8 bytes 求和后对 totalColors 取模 => colorIndex
    val colorIndex = striped.slice(0..7)
      .map { it.toUByte() }
      .map { it.toUInt() }
      .reduce { acc, i -> acc + i } % 32u
    //println("colorIndex:${colorIndex}")
    // 后 16 bytes 均分 8 组后每组对 2 个 bytes 求和再对 totalPositions 取模 => positionIndexes
    val trailingBytes = striped.slice(8 until striped.size).map { it.toUByte() }.map { it.toUInt() }
    //println("trailingBytes ${trailingBytes}")
    //println("trailingBytes size:${trailingBytes.size}")
    val positionIndexes = arrayOfNulls<Int>(8).mapIndexed { index, _ ->
      (trailingBytes[index * 2] + trailingBytes[index * 2 + 1]) % 64u
    }
    //println("positionIndexes:${positionIndexes.map { it.toInt() }}")
    return Triple(colorIndex.toInt(), positionIndexes.map { it.toInt() }, roleType)
  }
}