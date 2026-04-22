# Codex Handoff

## Project Snapshot

This is a Greenfoot Java simulation project for an RTS-style two-team battle/economy game.

Main entry point:

- `MyWorld.java` creates the world, teams, bases, resources, UI, and random supply drops.
- Open the folder in Greenfoot and run the scenario from the Greenfoot UI.
- There is no command-line build or automated test setup in the repository right now.

Current repository note:

- The working tree already has uncommitted changes in `Barrack.java`, `Base.java`, `Buildings.java`, `Team.java`, and `project.greenfoot`.
- Treat those as user/team changes. Do not revert them unless explicitly asked.

## Gameplay Overview

The simulation currently models:

- Red and Blue teams.
- Bases that spawn workers.
- Workers that mine team-side resources and deposit resources at their base.
- Barracks that spawn marines over time and can spawn officers through `addPeople()`.
- Defensive turrets that shoot enemies in range.
- Soldiers that search for enemies, chase, and fire projectiles.
- Random supply drops that grant resources, workers, and sometimes marines.
- Resource counters and health bars.

Default setup in `MyWorld`:

- World size: `1200 x 800`.
- Red team starts with strategy `ATK`.
- Blue team starts with strategy `ECO`.
- Red base is placed near `(200, 490)`.
- Blue base is placed near `(990, 165)`.
- Three resource nodes are placed near each side.
- One extra red barrack is currently added at `(200, 200)` for testing.

## Core Class Map

`MyWorld.java`

- Owns world setup and state changes.
- Creates both `Team` objects.
- Places bases, resources, UI, counters, and starting structures.
- Handles supply spawning through `spawnSupply()`.
- Provides distance helpers and closest-opponent lookup.

`Team.java`

- Stores team id, name, resources, units, buildings, strategy, cooldowns, base, barracks, and turrets.
- Strategies are `ECO`, `ATK`, and `DEF`.
- `setUpWorld()` creates initial workers/buildings based on strategy.
- `spawn()` handles later strategy-driven production.

Important caveat: `Team` extends `Actor`, but team objects are not currently added to the world in `MyWorld`. In Greenfoot, `act()` only runs for actors in the world, so `Team.act()` strategy cooldowns may not run unless teams are added to the world or updated manually from `MyWorld.act()`.

`Entity.java`

- Base class for anything with team, health, cost, and target logic.
- Handles death/removal.
- Provides opponent checks and target search.

`People.java`

- Base class for movable units.
- Adds health bars when placed in the world.
- Provides `moveTowards()`.

`Buildings.java`

- Base class for buildings.
- Registers buildings with their team.
- Adds health bars when placed in the world.
- Provides `addPeople(String type)`, currently only supporting `"Officer"`.

`Base.java`

- Building with 1000 health.
- Uses red/blue base images.
- `addPeople()` creates a `Worker` tied to this base.

`Worker.java`

- Moves between assigned resources and home base.
- Mines after a timer, then deposits resources.
- Tracks per-team resource assignment using static red/blue indexes.
- Contains early build-location helper methods, but building behavior is not fully connected.

`Barrack.java`

- Building with 500 health.
- Spawns a `Marine` every 300 acts.
- `addPeople()` currently creates an `Officer`.

`Soldier.java`, `Marine.java`, `Officer.java`

- `Soldier` implements shared combat behavior.
- `Marine` fires one bullet.
- `Officer` fires a five-shot spread.

`DefensiveTurret.java`

- Building with 700 health.
- Finds enemy entities in range and fires `TurretBullet`.

`Projectile.java`, `SoldierBullet.java`, `TurretBullet.java`

- Projectiles move forward, damage enemy entities on collision, and remove themselves at world edges.

`Supply.java`

- Random drop actor.
- On pickup, gives resources and units to the collecting team.
- Can spawn marines only if the team has a barrack.

`HealthBar.java`, `ResourceCounter.java`, `UI.java`

- Visual support actors.

`SuperSmoothMover.java`

- Imported/utility superclass for precise double-based movement and rotation.

## Assets

Game images are under `images/`.

Important names used directly by code:

- `Background.png`
- `Ui.png`
- `RedHomeBase.png`, `BlueHomeBase.png`
- `RedBarracks.png`, `BlueBarracks.png`
- `RedTurret.png`, `BlueTurret.png`
- `RedWorkerRegular.png`, `BlueWorkerRegular.png`
- `RedWorkerMining.png`, `BlueWorkerMining.png`
- `RedMarine.png`, `BlueMarine.png`
- `RedMarineRecoil.png`, `BlueMarineRecoil.png`
- `RedOfficer.png`, `RedOfficerRecoil.png`
- `Supply.png`
- `Resources1.png`, `Resources2.png`
- `soldierBullet.png`

Blue officer images are not currently listed in the repository file list. Creating a blue `Officer` may fail unless `BlueOfficer.png` and `BlueOfficerRecoil.png` are added or the image logic is changed.

## Current Risk List

These are worth checking before adding major features:

1. `Team.act()` may not execute because `Team` actors are not added to the world.
2. `Team.setStrategy(String Strategy)` ignores its parameter and assigns `this.strategy = strategy`.
3. `Supply.moveToTargetPosition()` currently uses `setLocation(targetX, getY() - dropSpeed)` even though supply starts above the world; this likely moves it farther upward instead of downward.
4. `Soldier.closeEnough()` uses raw coordinate sums instead of absolute distance, so idle movement can behave incorrectly.
5. `Soldier.setupImage()` calls `setImage(img)` while `img` is never assigned in that method; it later sets `emptyImg`, so this may be harmless but should be cleaned.
6. `Worker.build()` can create repeated turrets because it does not change state after building, and the build state does not appear fully wired.
7. `Worker.move()` checks `state.equals("build")`, but the state used elsewhere is `"building"`.
8. `Projectile.act()` assumes `targetHit.getTeam()` and `shooter.getTeam()` are non-null.
9. `Buildings.addPeople(String type)` only supports `"Officer"` now. Older comments/code imply it once supported workers or soldiers.
10. `Base.setupImage()` scales `img` before the null check. This is fine when team is valid, but fragile.

## Suggested Next Steps

1. Decide whether teams should be real world actors or plain controller objects.
   - If actors: add `redTeam` and `blueTeam` to the world, possibly off-screen or invisible.
   - If controllers: call an update method from `MyWorld.act()`.

2. Fix the obvious state/logic bugs first.
   - `Team.setStrategy`
   - `Supply` drop direction
   - `Worker` build state naming
   - `Soldier.closeEnough`

3. Make unit/building spawning consistent.
   - Decide whether `Base.addPeople()` and `Barrack.addPeople()` should return the spawned unit.
   - Decide whether `Buildings.addPeople(String type)` should be a generic factory or removed in favor of explicit methods.

4. Add missing assets or defensive fallbacks.
   - Especially blue officer images if blue teams can spawn officers.

5. Add a short manual test checklist in `README.TXT`.
   - Open in Greenfoot.
   - Compile.
   - Run for 30 seconds.
   - Confirm workers mine/deposit.
   - Confirm barracks spawn units.
   - Confirm soldiers/turrets damage enemies.
   - Confirm supply drops move downward and can be picked up.

## Development Notes

- Prefer keeping class names and image filenames aligned because several image loads are built from class names.
- Greenfoot metadata in `project.greenfoot` changes often when the IDE layout or dependency graph changes. Review it separately from behavior changes.
- Keep generated health bars and resource counters as actors; they self-update in `act()`.
- Avoid large refactors until basic gameplay loop issues are fixed and manually tested in Greenfoot.
