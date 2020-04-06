package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers - trips.map { it.driver }

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
//    allPassengers - trips.flatMap { it.passengers }.groupBy { it }.map { it.key to it.value.size }.filter { minTrips > it.second }.map { it.first }.toSet()
//    allPassengers - trips.flatMap { it.passengers }.groupBy { it }.filterValues { minTrips > it.size }.keys
    allPassengers.filter { trips.filter { trip -> it in trip.passengers }.count() >= minTrips }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    trips.filter { driver == it.driver }.flatMap { it.passengers }.groupBy { it }.filterValues { 1 < it.size }.keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
//    trips.flatMap { t -> t.passengers.map { it to t.discount } }.groupBy({ it.first }, { it.second }).filter {
//        it.value.filterNotNull().size > it.value.filter { it == null }.size
//    }.keys
    allPassengers.filter { p -> trips.filter { p in it.passengers && null != it.discount }.size > trips.filter { p in it.passengers && null == it.discount }.size }.toSet()


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return trips.map { it.duration }.groupBy { (it / 10) * 10 }.map { it.key to it.value.size }.maxBy { it.second }
        ?.let { IntRange(it.first, it.first + 9) }
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val sorted =
        trips.map { it.driver to it.cost }.groupBy({ it.first }, { it.second }).map { it.key to it.value.sum() }
            .sortedByDescending { it.second }
    val toIndex = (this.allDrivers.count() * 0.2).toInt()
    val adjustedToIndex = if (toIndex < sorted.size) toIndex else sorted.size
    val coreDriverTotal = sorted.subList(0, adjustedToIndex).map { it.second }.sum()
    val total = sorted.map { it.second }.sum()
    return (coreDriverTotal / total) >= 0.8
}