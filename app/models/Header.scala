package models

case class Header(idx: Int,
                  id: String,
                  version: Int,
                  parentId: String,
                  adProofsRoot: String,
                  stateRoot: String,
                  transactionsRoot: String,
                  timestamp: Long,
                  height: Int,
                  nonce: Long,
                  difficulty: Long,
                  equihashSolution: String,
                  txCount: Int,
                  minerAddress: String,
                  minerReward: Long)
