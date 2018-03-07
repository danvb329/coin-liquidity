$.tablesorter.addParser({
    id: 'thousands',
    is: function(s) {
        return false;
    },
    format: function(s) {
        if (s === 'n/a') return "99999999999999";
        return s.replace('$','').replace(/,/g,'');
    },
    type: 'numeric'
});

$(function() {
    $("table").tablesorter({
        headers: {
            2: { sorter:'thousands' },
            3: { sorter:'thousands' },
            4: { sorter:'thousands' },
            5: { sorter:'thousands' },
            6: { sorter:'thousands' },
            7: { sorter:'thousands' },
            8: { sorter:'thousands' },
            9: { sorter:'thousands' },
            10: { sorter:'thousands' }
        },
        sortList: [[3,1]]
    });
});