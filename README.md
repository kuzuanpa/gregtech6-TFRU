# gregtech6-TFRU

Fork of GregTech 6 maintained for the TFRU pack.

## Fork focus
This README only describes local fork work authored in this repository by the TFRU maintainers, excluding upstream patches that were later merged in.

## Notable local changes
- Reworked a large set of TFRU-specific recipes and progression details, including Glyceryl, Carbon Crucible, Melter/Smelter flow, Oil Distill, rough glass, steam turbine cost, laser shell cost, and other value changes.
- Added or refined machine behavior such as the `POWER_SAVING` state, higher-than-64 basic machine parallel scaling, rate-limited tanks, meter-detectable converters, valve/relay controller interactions, and output/full-state safeguards.
- Built extensive WAILA support, tooltip improvements, and recipe-origin visibility, including automatic `From` tagging and richer tile information.
- Added or adjusted pack compatibility hooks for TFRU, TFC armor and hazmat handling, kTFRUAddon integration, specified TFRU world generation, seawater generation, and environment detection.
- Changed content presentation and cleanup in multiple places, including circuit textures, hidden unused materials, translation/i18n work, logging cleanup, and transparent/glowing texture compatibility fixes.
- Added Maven publication setup and follow-up publish-position cleanup while keeping the fork buildable.

## Build / publish notes
- Gradle build files and license files are present in the repository root.
- Local history explicitly includes `init maven publish`, publication cleanup, and repeated TFRU-targeted maintenance commits.
- This README intentionally excludes upstream GT6 work that exists independently of the fork.
