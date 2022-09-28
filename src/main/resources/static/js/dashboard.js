/* globals Chart:false, feather:false */

(async () => {
        'use strict'
        feather.replace({'aria-hidden': 'true'});
        if (document.getElementById('weekChart')) {
            // feather.replace({'aria-hidden': 'true'});

            let weekDay;
            let mondayCount;
            let tuesdayCount;
            let wednesdayCount;
            let thursdayCount;
            let fridayCount;
            let saturdayCount;
            let sundayCount;

            await fetch("/payment/orderCount", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            })
                .then(result => result.json())
                .then(res => {
                    console.log(res);
                    console.log(res[0]);
                    for (let i = 0; i < res.length; i++) {
                        console.log(res[i].weekDay);
                        switch (res[i].weekDay) {
                            case '월요일' :
                                mondayCount = res[i].weekCount;
                                break;
                            case '화요일' :
                                tuesdayCount = res[i].weekCount;
                                break;
                            case '수요일' :
                                wednesdayCount = res[i].weekCount;
                                break;
                            case '목요일' :
                                thursdayCount = res[i].weekCount;
                                break;
                            case '금요일' :
                                fridayCount = res[i].weekCount;
                                break;
                            case '토요일' :
                                saturdayCount = res[i].weekCount;
                                break;
                            case '일요일' :
                                sundayCount = res[i].weekCount;
                                break;
                        }
                    }
                    // Graphs
                    const ctx = document.getElementById('weekChart')
                    // eslint-disable-next-line no-unused-vars
                    const myChart = new Chart(ctx, {
                        type: 'line',
                        data: {
                            labels: [
                                'Sunday',
                                'Monday',
                                'Tuesday',
                                'Wednesday',
                                'Thursday',
                                'Friday',
                                'Saturday'
                            ],
                            datasets: [{
                                data: [
                                    sundayCount,
                                    mondayCount,
                                    tuesdayCount,
                                    wednesdayCount,
                                    thursdayCount,
                                    fridayCount,
                                    saturdayCount,
                                    sundayCount
                                ],
                                lineTension: 0,
                                backgroundColor: 'transparent',
                                borderColor: '#007bff',
                                borderWidth: 4,
                                pointBackgroundColor: '#007bff'
                            }]
                        },
                        options: {
                            scales: {
                                yAxes: [{
                                    ticks: {
                                        beginAtZero: true
                                    }
                                }]
                            },
                            legend: {
                                display: false
                            }
                        }
                    })
                })
                .catch((error) => error.text().then((res) => {
                    alert(res)
                }));
        }


        if (document.getElementById('modelChart')) {
            // feather.replace({'aria-hidden': 'true'});

            let disneyCount;
            let hosodaCount;
            let hayaoCount;
            let shinkaiCount;
            let paprikaCount;
            let celebaCount;
            let facev1Count;
            let facev2Count;
            let webtoonPaprikaCount;

            await fetch("/payment/model", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            })
                .then(result => result.json())
                .then(res => {
                    console.log(res);
                    console.log(res[0]);
                    const arraySize = res.length;
                    console.log(arraySize);
                    for (let i = 0; i < arraySize; i++) {
                        switch (res[i].modelName) {
                            case '디즈니' :
                                disneyCount = res[i].count;
                                break;
                            case '초상화 Hosoda' :
                                hosodaCount = res[i].count;
                                break;
                            case '초상화 Hayao' :
                                hayaoCount = res[i].count;
                                break;
                            case '초상화 Shinkai' :
                                shinkaiCount = res[i].count;
                                break;
                            case '초상화 Paprika' :
                                paprikaCount = res[i].count;
                                break;
                            case '웹툰 celeba' :
                                celebaCount = res[i].count;
                                break;
                            case '웹툰 facev1' :
                                facev1Count = res[i].count;
                                break;
                            case '웹툰 facev2' :
                                facev2Count = res[i].count;
                                break;
                            case '웹툰 paprika' :
                                webtoonPaprikaCount = res[i].count;
                                break;
                        }
                    }
                    // Graphs
                    const ctx = document.getElementById('modelChart')
                    // eslint-disable-next-line no-unused-vars
                    const myChart = new Chart(ctx, {
                        type: 'line',
                        data: {
                            labels: [
                                '디즈니',
                                '초상화 Hosoda',
                                '초상화 Hayao',
                                '초상화 Shinkai',
                                '초상화 Paprika',
                                '웹툰 celeba',
                                '웹툰 facev1',
                                '웹툰 facev2',
                                '웹툰 paprika'
                            ],
                            datasets: [{
                                data: [
                                    disneyCount,
                                    hosodaCount,
                                    hayaoCount,
                                    shinkaiCount,
                                    paprikaCount,
                                    celebaCount,
                                    facev1Count,
                                    facev2Count,
                                    webtoonPaprikaCount
                                ],
                                lineTension: 0,
                                backgroundColor: 'transparent',
                                borderColor: '#007bff',
                                borderWidth: 4,
                                pointBackgroundColor: '#007bff'
                            }]
                        },
                        options: {
                            scales: {
                                yAxes: [{
                                    ticks: {
                                        beginAtZero: true
                                    }
                                }]
                            },
                            legend: {
                                display: false
                            }
                        }
                    })
                })
                // .catch((error) => error.text().then((res) => {
                //     alert(res)
                // }));
        }
    }
)()