package com.lra.kalanikethan.data.repository

import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.FamilyWithID
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.util.Tables
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count

class FamilyRepository {
    private val client = SupabaseClientProvider.client

    suspend fun getLastFamilyID(): Int {
        val count = client.from(Tables.FAMILIES)
            .select {
                count(Count.EXACT)
            }
            .countOrNull()!!
        return count.toInt()
    }

    suspend fun addFamily(family: Family): String? {
        client.from(Tables.FAMILIES).insert(family)
        val familyID = client.from(Tables.FAMILIES).select() {
            filter {
                FamilyWithID::familyName eq family.familyName
                FamilyWithID::email eq family.email
            }
        }.decodeSingle<FamilyWithID>()
        return familyID.familyID
    }

    suspend fun getFamilyFromID(id: String): FamilyWithID {
        return client.from(Tables.FAMILIES).select {
            filter {
                FamilyWithID::familyID eq id
            }
        }.decodeSingle<FamilyWithID>()
    }

    suspend fun addParent(parent: Parent) {
        client.from(Tables.PARENTS).insert(parent)
    }

    suspend fun getParentsByFamilyId(familyId: String): List<Parent> {
        return client.from(Tables.PARENTS).select {
            filter {
                Parent::familyId eq familyId
            }
        }.decodeList<Parent>()
    }

    suspend fun updateParent(parent: Parent) {
        client.from(Tables.PARENTS).upsert(parent) {
            onConflict = "parent_id"
        }
    }

    suspend fun deleteParent(parentId: Int) {
        client.from(Tables.PARENTS).delete {
            filter {
                Parent::parentId eq parentId
            }
        }
    }

    suspend fun updateFamily(family: FamilyWithID) {
        client.from(Tables.FAMILIES).update({
            FamilyWithID::familyName setTo family.familyName
            FamilyWithID::email setTo family.email
        }) {
            filter {
                FamilyWithID::familyID eq family.familyID
            }
        }
    }
}
