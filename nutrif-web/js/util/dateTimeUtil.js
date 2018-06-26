/*
 *  Funções de gerenciamento do Toast.
 */
nutrIFApp.factory("dateTimeUtil", function ($mdToast) {
    return {
        timeToDate: function (timeStr) {
            let now = new Date();
            let res = timeStr.split(":");
            now.setHours(res[0]);
            now.setMinutes(res[1]);
            now.setSeconds(0);
            return now;
        },
        
        millisToDate: function (milli) {
            let date = new Date(milli);
            return date;
        },
        
        toDate: function (stringDate) {
            let date = new Date(stringDate);
            return date;
        }
    }
});