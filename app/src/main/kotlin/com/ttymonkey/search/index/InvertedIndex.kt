package com.ttymonkey.search.index

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlinx.serialization.*
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

typealias Position = Int
typealias DocumentId = Int
typealias Document = String

@Serializable
class InvertedIndex {
    private var index: MutableMap<String, MutableMap<DocumentId, MutableList<Position>>> = mutableMapOf()
    private val documentsIndex: MutableMap<DocumentId, Document> = mutableMapOf()

    @Transient
    private val currentDocumentId: AtomicInteger = AtomicInteger(1)

    @Transient
    private val lock = ReentrantReadWriteLock()

    fun addTokens(file: File, tokens: List<String>) = lock.write {
        val documentId = currentDocumentId.getAndIncrement()
        documentsIndex[documentId] = file.toString()

        var position = 0
        tokens.forEach {
            ++position
            if (!index.containsKey(it)) {
                index[it] = mutableMapOf()
            }

            if (index[it]!!.containsKey(documentId)) {
                index[it]!![documentId]?.add(position)
            } else {
                index[it]!![documentId] = mutableListOf(position)
            }
        }
    }

    fun getPositions(tokens: List<String>): Map<Document, List<Position>> = lock.read {
        if (tokens.isEmpty()) {
            return mapOf()
        }

        var previousTokenToDocuments = getTokenPositions(tokens[0])
        for (idx in 1 until tokens.size) {
            val currentTokenToDocuments = getTokenPositions(tokens[idx])
            val continuousTokenPositions : MutableMap<DocumentId, MutableList<Position>> = mutableMapOf()

            val documentsIntersection = previousTokenToDocuments.keys.intersect(currentTokenToDocuments.keys)

            documentsIntersection.forEach { documentId ->
                val previousPositions = previousTokenToDocuments.getValue(documentId)
                val currentPositions = currentTokenToDocuments.getValue(documentId)

                var ppPointer = 0
                var cpPointer = 0

                while (ppPointer < previousPositions.size && cpPointer < currentPositions.size) {
                    when {
                        currentPositions[cpPointer] == previousPositions[ppPointer] + 1 -> {
                            if (!continuousTokenPositions.containsKey(documentId)) {
                                continuousTokenPositions[documentId] = mutableListOf()
                            }

                            continuousTokenPositions[documentId]?.add(currentPositions[cpPointer])
                            ++ppPointer
                            ++cpPointer
                        }
                        currentPositions[cpPointer] < previousPositions[ppPointer] + 1 -> {
                            ++cpPointer
                        }
                        else -> {
                            ++ppPointer
                        }
                    }
                }
            }

            previousTokenToDocuments = continuousTokenPositions
        }

        val documentsPositions : MutableMap<Document, List<Position>> = mutableMapOf()
        previousTokenToDocuments.forEach { (documentId, positions) ->
            documentsPositions[documentsIndex[documentId]!!] = positions.map{ it - tokens.size + 1  }
        }

        return documentsPositions
    }

    private fun getTokenPositions(token: String): Map<DocumentId, List<Position>>{
        return index[token] ?: mutableMapOf()
    }
}
