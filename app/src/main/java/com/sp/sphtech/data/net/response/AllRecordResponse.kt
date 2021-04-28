package com.sp.sphtech.data.net.response

import com.sp.sphtech.data.model.RecordBean

class AllRecordResponse {
    var help: String? = null
    var success = false
    var result: ResultBean? = null

    class ResultBean {
        var resource_id: String? = null
        /**
         * start : /api/action/datastore_search?limit=100&amp;resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f
         * next : /api/action/datastore_search?offset=100&amp;limit=100&amp;resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f
         */
        private var _links: LinksBean? = null
        var limit = 0
        var total = 0

        /**
         * type : int4
         * id : _id
         */
        var fields: List<FieldsBean>? = null

        /**
         * volume_of_mobile_data : 0.000384
         * quarter : 2004-Q3
         * _id : 1
         */
        var records: List<RecordBean>? = null

        fun get_links(): LinksBean? {
            return _links
        }

        fun set_links(_links: LinksBean?) {
            this._links = _links
        }

        class LinksBean {
            var start: String? = null
            var next: String? = null

        }

        class FieldsBean {
            var type: String? = null
            var id: String? = null

        }

    }
}