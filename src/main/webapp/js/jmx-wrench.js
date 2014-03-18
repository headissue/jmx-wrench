function initTypeahead(servletPrefix) {
  console.log("init typeahead:" + servletPrefix);
  var engine = new Bloodhound({
    name: 'search',
    remote:  {
      url:  servletPrefix + "/api?q=%QUERY",
      filter: function (parsedResponse) {
        // parsedResponse is the array returned from your backend
        console.log(parsedResponse.suggestions);

        // do whatever processing you need here
        return parsedResponse.suggestions;
      }
    },
    datumTokenizer: function(d) {
      var res = Bloodhound.tokenizers.whitespace('name');
      console.log(res);
      return res;
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace
  });

  engine.initialize();

  $('#search').typeahead({
    minLength: 3,
    highlight: true
  }, {
    source: engine.ttAdapter(),
    displayKey: 'name',
    templates: {
      empty: [
        '<div class="empty-message">',
        'No classes found',
        '</div>'
      ].join('\n'),
      suggestion: Handlebars.compile('<a href="{{url}}"><strong>{{name}}</strong></a>')
    }
  });
}